package com.teampolymer.polymer.core.api.storage.io;

import java.util.List;

/** 发出输入请求的东西 */
public interface IInputContext<T> {
    List<T> get();
}
