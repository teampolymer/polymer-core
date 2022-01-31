package com.teampolymer.polymer.core.api.multiblock;

import com.teampolymer.polymer.core.api.component.IMultiblockComponent;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;
import com.teampolymer.polymer.core.api.util.IAttributeProvider;
import net.minecraft.util.math.vector.Vector3i;

import java.util.List;

/**
 * 抽象的多方快结构
 */
public interface IMultiblock extends IAttributeProvider {
    /**
     * 获取该多方快所有的组件
     *
     * @return 组件列表
     */
    List<IMultiblockComponent> getComponents();

    /**
     * 获取该多方快绑定的机械
     *
     * @return 机械
     */
    String getMachine();

    /**
     * 判断该多方快结构是否是分布式结构
     */
    boolean isDistributedMachine();


    /**
     * 获取多方快结构的尺寸
     *
     * @return 尺寸
     */
    Vector3i getSize();
    IMultiblockPart getCore();


}
