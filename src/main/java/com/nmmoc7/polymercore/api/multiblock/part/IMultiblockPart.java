package com.nmmoc7.polymercore.api.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * 多方快结构的一个“组件”
 * 一般是值某个相对位置可能出现的其他方块
 * 这个接口表示的部件会影响整个多方快结构整体
 * 同时数量等也会有所限制
 */
public interface IMultiblockPart {
    @Nullable
    IPartChoice pickupChoice(BlockState possible, MultiblockDirection direction);

    @Nullable
    IPartChoice pickupChoice(@Nullable String type);

    @Nullable
    IPartChoice defaultChoice();

    /**
     * 当前坐标所有可能的Entry
     */
    Collection<IPartChoice> choices();

    /**
     * 投影组件时的默认标签
     */
    Collection<IPartChoice> sampleChoices();

}
