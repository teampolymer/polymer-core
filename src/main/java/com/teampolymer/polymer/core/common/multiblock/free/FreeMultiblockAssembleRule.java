package com.teampolymer.polymer.core.common.multiblock.free;

import com.teampolymer.polymer.core.api.multiblock.IDefinedMultiblock;
import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockUnit;
import com.teampolymer.polymer.core.api.multiblock.part.IPartChoice;
import com.teampolymer.polymer.core.api.util.PositionUtils;
import com.teampolymer.polymer.core.common.multiblock.assembled.AbstractAssembleRule;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class FreeMultiblockAssembleRule extends AbstractAssembleRule {
    public FreeMultiblockAssembleRule(BlockPos offset, boolean isSymmetrical, Rotation rotation) {
        super(offset, isSymmetrical, rotation);
    }

    public FreeMultiblockAssembleRule() {

    }

    @Override
    public Map<BlockPos, IMultiblockUnit> mapParts(IDefinedMultiblock originalMultiblock) {
        Map<Vector3i, IMultiblockPart> parts = originalMultiblock.getParts();
        Map<BlockPos, IMultiblockUnit> results = new HashMap<>();
        MultiblockDirection direction = MultiblockDirection.get(getRotation(), isSymmetrical());
        for (Map.Entry<Vector3i, IMultiblockPart> partEntry : parts.entrySet()) {
            IMultiblockPart part = partEntry.getValue();
            Vector3i relativePos = partEntry.getKey();
            String choiceType = getChoiceType(relativePos);
            IPartChoice choice = part.pickupChoice(choiceType);
            if (choice == null) {
                return null;
            }
            IMultiblockUnit unit = choice.getUnit(direction);
            BlockPos pos = PositionUtils.applyModifies(relativePos, getOffset(), getRotation(), isSymmetrical());
            results.put(pos, unit);
        }
        return results;
    }
}
