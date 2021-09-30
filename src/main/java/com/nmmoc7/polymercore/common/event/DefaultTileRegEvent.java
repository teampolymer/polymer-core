package com.nmmoc7.polymercore.common.event;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import java.util.ArrayList;

/**
 * 如果想使用自带的tileentity，请在这个事件注册
 */
public class DefaultTileRegEvent extends Event implements IModBusEvent {
    ArrayList<Block> list = new ArrayList<>();
    final TileEntityType<?> type;

    public DefaultTileRegEvent(TileEntityType<?> type) {
        this.type = type;
    }

    public void addAll(Block... blocks) {
        for (Block block : blocks) {
            add(block);
        }
    }

    public void add(Block block) {
        list.add(block);
    }

    public ArrayList<Block> getList() {
        return list;
    }

    public TileEntityType<?> getType() {
        return type;
    }
}
