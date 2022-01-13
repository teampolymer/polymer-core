package com.nmmoc7.polymercore.client.utils;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import com.nmmoc7.polymercore.client.utils.multiblock.ISampleProvider;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class SchematicRenderUtils {

    public static IModelData findModelData(BlockState block, Vector3i relativePos, IDefinedMultiblock multiblock) {
        return EmptyModelData.INSTANCE;
    }

    public static BlockState pickupSampleBlock(int renderTicks, ISampleProvider part) {
        int i = (renderTicks) / 20;
        return part.getSample(i);
    }

    @NotNull
    public static Vector4f transformCamera(Vector3d view, SchematicTransform transform) {
        //转换视角
        Matrix4f result = Matrix4f.createTranslateMatrix(0.5f, 0.5f, 0.5f);
        if (MathHelper.abs(transform.flip) > 0.1f) {
            result.multiply(Matrix4f.createScaleMatrix(1 / transform.flip, 1f, 1f));
        }
        result.multiply(Vector3f.YP.rotationDegrees(-transform.rotation));
        result.multiply(Matrix4f.createTranslateMatrix(-0.5f, -0.5f, -0.5f));
        result.multiply(Matrix4f.createTranslateMatrix(-transform.x, -transform.y, -transform.z));
        Vector4f viewRelative = new Vector4f((float) view.x, (float) view.y, (float) view.z, 1f);
        viewRelative.transform(result);
        return viewRelative;
    }

    public static SortedMap<Double, Vector3i> sortByDistance(Collection<Vector3i> positions, Vector4f viewRelative) {

        Double2ObjectRBTreeMap<Vector3i> map = new Double2ObjectRBTreeMap<>(DoubleComparators.OPPOSITE_COMPARATOR);
        for (Vector3i relativePos : positions) {
            double distanceSq = relativePos.distSqr(viewRelative.x(), viewRelative.y(), viewRelative.z(), true);
            map.put(distanceSq, relativePos);
        }
        return map;
    }
}
