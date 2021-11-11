package com.nmmoc7.polymercore.common.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.block.BlockState;

import java.util.List;

public class DefaultMultiblockPart implements IMultiblockPart {
    private List<PartEntry> entries;
    private List<String> defaultTags;

    public DefaultMultiblockPart(List<PartEntry> entries, List<String> defaultTags) {
        this.entries = entries;
        this.defaultTags = defaultTags;
    }

    @Override
    public PartEntry pickupUnit(BlockState possible) {
        for (PartEntry entry : entries) {
            if (entry.getUnit().test(possible)) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public List<PartEntry> entries() {
        return entries;
    }

    @Override
    public List<String> defaultTags() {
        return defaultTags;
    }

    public static class DefaultPartEntry implements PartEntry {
        private List<String> tags;
        private IMultiblockUnit unit;

        public DefaultPartEntry(List<String> tags, IMultiblockUnit unit) {
            this.tags = tags;
            this.unit = unit;
        }

        @Override
        public IMultiblockUnit getUnit() {
            return unit;
        }

        @Override
        public List<String> getTags() {
            return tags;
        }
    }
}
