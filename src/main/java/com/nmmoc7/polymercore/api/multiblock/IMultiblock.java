package com.nmmoc7.polymercore.api.multiblock;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.util.IAttributeProvider;
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
     * 获取多方快结构的尺寸
     *
     * @return 尺寸
     */
    Vector3i getSize();
    IMultiblockPart getCore();


}
