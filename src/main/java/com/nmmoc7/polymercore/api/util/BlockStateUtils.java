package com.nmmoc7.polymercore.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class BlockStateUtils {
    private static final Logger LOG = LogManager.getLogger();

    public static BlockState getWithProperties(Block block, Map<String, String> properties) {
        BlockState state = block.defaultBlockState();
        StateContainer<Block, BlockState> stateContainer = block.getStateDefinition();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            Property<?> property = stateContainer.getProperty(entry.getKey());
            state = withProperty(state, property, entry.getValue());
        }
        return state;
    }

    public static List<BlockState> getAllMatchingProperties(Block block, Map<String, String> properties) {
        Map<Property<?>, Comparable<?>> remapped = new HashMap<>();
        StateContainer<Block, BlockState> stateContainer = block.getStateDefinition();
        for (Map.Entry<String, String> prop : properties.entrySet()) {
            Property<?> property = stateContainer.getProperty(prop.getKey());
            if (property != null) {
                Optional<?> optional = property.getValue(prop.getValue());
                if (optional.isPresent()) {
                    remapped.put(property, property.getValueClass().cast(optional.get()));
                } else {
                    LOG.warn("Unable to read property: {} with value: {} for block: {}", prop.getKey(), prop.getValue(), block.toString());
                }
            }
        }

        List<BlockState> result = new ArrayList<>();
        for (BlockState validState : block.getStateDefinition().getPossibleStates()) {
            for (Map.Entry<Property<?>, Comparable<?>> entry : remapped.entrySet()) {
                if (Objects.equals(entry.getValue(), validState.getValue(entry.getKey()))) {
                    result.add(validState);
                }
            }
        }
        return result;
    }

    public static <T extends Comparable<T>> BlockState withProperty(BlockState state, Property<T> key, String value) {
        if (key == null) {
            return state;
        }
        Optional<T> optional = key.getValue(value);
        if (optional.isPresent()) {
            return state.setValue(key, optional.get());
        } else {
            LOG.warn("Unable to read property: {} with value: {} for blockstate: {}", key, value, state.toString());
            return state;
        }
    }


}
