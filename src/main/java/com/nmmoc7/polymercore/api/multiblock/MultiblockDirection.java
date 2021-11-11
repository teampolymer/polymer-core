package com.nmmoc7.polymercore.api.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.vector.Orientation;

import java.util.EnumMap;

public enum MultiblockDirection {
    NONE(Rotation.NONE, false),
    CLOCKWISE_90(Rotation.CLOCKWISE_90, false),
    CLOCKWISE_180(Rotation.CLOCKWISE_180, false),
    COUNTERCLOCKWISE_90(Rotation.COUNTERCLOCKWISE_90, false),
    NONE_FLIPPED(Rotation.NONE, true),
    CLOCKWISE_90_FLIPPED(Rotation.CLOCKWISE_90, true),
    CLOCKWISE_180_FLIPPED(Rotation.CLOCKWISE_180, true),
    COUNTERCLOCKWISE_90_FLIPPED(Rotation.COUNTERCLOCKWISE_90, true),
    ;

    private final Rotation rotation;
    private final boolean flipped;

    MultiblockDirection(Rotation rotation, boolean flipped) {
        this.rotation = rotation;
        this.flipped = flipped;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public static final MultiblockDirection[] DIRECTIONS;

    public static MultiblockDirection get(Rotation rotation, boolean flipped) {
        if (flipped)
            return flippedDirections.get(rotation);
        return nonFlippedDirections.get(rotation);
    }


    private static final EnumMap<Rotation, MultiblockDirection> nonFlippedDirections;
    private static final EnumMap<Rotation, MultiblockDirection> flippedDirections;

    static {
        DIRECTIONS = values();
        nonFlippedDirections = new EnumMap<>(Rotation.class);
        flippedDirections = new EnumMap<>(Rotation.class);
        for (MultiblockDirection direction : DIRECTIONS) {
            if (direction.isFlipped()) {
                flippedDirections.put(direction.getRotation(), direction);
            } else {
                nonFlippedDirections.put(direction.getRotation(), direction);
            }
        }
    }
}
