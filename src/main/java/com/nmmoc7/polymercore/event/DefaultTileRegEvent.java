package com.nmmoc7.polymercore.event;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import java.util.ArrayList;

/**
 * 如果想使用自带的tileentity，请在这个事件注册
 */
public class DefaultTileRegEvent extends Event implements IModBusEvent {
    ArrayList<Block> list = new ArrayList<>();

    public void add(Block block) {
        list.add(block);
    }

    public ArrayList<Block> getList() {
        return list;
    }
}
