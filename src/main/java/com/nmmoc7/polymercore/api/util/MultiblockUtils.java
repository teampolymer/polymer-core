package com.nmmoc7.polymercore.api.util;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.registry.PolymerCoreRegistries;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.EnumMap;
import java.util.Objects;

public class MultiblockUtils {
    public static IAssembledMultiblock deserializeNBT(World world, INBT nbt) {
        if (!(nbt instanceof CompoundNBT)) {
            return null;
        }
        CompoundNBT compound = (CompoundNBT) nbt;
        String type = compound.getString("type");
        IMultiblockType typeObj = PolymerCoreRegistries.MULTIBLOCK_TYPES.getValue(new ResourceLocation(type));
        if (typeObj == null) {
            return null;
        }
        return typeObj.createFromNBT(world, compound);
    }

    private static EnumMap<Direction, Vector3i> multiblockEdgeCache = null;
    private static ResourceLocation cachedName = null;

    /**
     * 计算一个多方快结构某个方向上最边缘的方块
     */
    private static EnumMap<Direction, Vector3i> getMultiblockEdge(IDefinedMultiblock multiblock) {
        if (!Objects.equals(multiblock.getRegistryName(), cachedName)) {
            multiblockEdgeCache = null;
        }
        if (multiblockEdgeCache == null) {
            //y
            Vector3i d = Vector3i.ZERO;
            Vector3i u = Vector3i.ZERO;
            int dd = 0;
            int ud = 0;
            //z
            Vector3i n = Vector3i.ZERO;
            Vector3i s = Vector3i.ZERO;
            int nd = 0;
            int sd = 0;
            //x
            Vector3i w = Vector3i.ZERO;
            Vector3i e = Vector3i.ZERO;
            int wd = 0;
            int ed = 0;
            for (Vector3i vector3i : multiblock.getParts().keySet()) {
                int distance = vector3i.distManhattan(Vector3i.ZERO);
                if (vector3i.getX() > e.getX() || (vector3i.getX() == e.getX() && distance < ed)) {
                    e = vector3i;
                    ed = distance;
                }
                if (vector3i.getY() > u.getY() || (vector3i.getY() == u.getY() && distance < ud)) {
                    u = vector3i;
                    ud = distance;
                }
                if (vector3i.getZ() > s.getZ() || (vector3i.getZ() == s.getZ() && distance < sd)) {
                    s = vector3i;
                    sd = distance;
                }
                if (vector3i.getX() < w.getX() || (vector3i.getX() == w.getX() && distance < wd)) {
                    w = vector3i;
                    wd = distance;
                }
                if (vector3i.getY() < d.getY() || (vector3i.getY() == d.getY() && distance < dd)) {
                    d = vector3i;
                    dd = distance;
                }
                if (vector3i.getZ() < n.getZ() || (vector3i.getZ() == n.getZ() && distance < nd)) {
                    n = vector3i;
                    nd = distance;
                }
            }

            multiblockEdgeCache = new EnumMap<>(Direction.class);
            multiblockEdgeCache.put(Direction.DOWN, d);
            multiblockEdgeCache.put(Direction.UP, u);
            multiblockEdgeCache.put(Direction.NORTH, n);
            multiblockEdgeCache.put(Direction.SOUTH, s);
            multiblockEdgeCache.put(Direction.WEST, w);
            multiblockEdgeCache.put(Direction.EAST, e);
            cachedName = multiblock.getRegistryName();
        }

        return multiblockEdgeCache;
    }


    /**
     * 获取多方快放置最合适的位置
     */
    public static BlockPos findMostSuitablePosition(IDefinedMultiblock multiblock, BlockPos hovering, Direction face, Rotation rotation, boolean isFlipped) {
        EnumMap<Direction, Vector3i> multiblockEdge = getMultiblockEdge(multiblock);

        if (face.getAxis() == Direction.Axis.Y) {
            return hovering.relative(face).subtract(multiblockEdge.get(face.getOpposite()));
        }

        Direction direction = Direction.from2DDataValue((face.getOpposite().get2DDataValue() - rotation.ordinal() + 4) % 4);
        if (isFlipped && direction.getAxis() == Direction.Axis.X) {
            direction = direction.getOpposite();
        }

        BlockPos offset = PositionUtils.applyModifies(multiblockEdge.get(direction), BlockPos.ZERO, rotation, isFlipped);
        return hovering.relative(face).subtract(offset);
    }
}
