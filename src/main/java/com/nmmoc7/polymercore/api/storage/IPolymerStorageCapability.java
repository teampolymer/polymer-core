package com.nmmoc7.polymercore.api.storage;

import com.nmmoc7.polymercore.api.storage.io.IInputContext;
import com.nmmoc7.polymercore.api.storage.io.IOutputContext;
import com.nmmoc7.polymercore.api.storage.io.IReplyContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPolymerStorageCapability<T, I extends IInputContext<T>, O extends IOutputContext<T>, R extends IReplyContext<T>>
        extends INBTSerializable<CompoundNBT> {

    /** 尝试添加 */
    R tryAdd(I context);

    /** 尝试获取 */
    R tryGet(O context);

    /** 用于前后端同步 */
    default void sync() {
        // todo
    }
}
