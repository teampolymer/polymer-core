package com.nmmoc7.polymercore.api.storage.io;

import java.util.List;

public interface IReplyContext<T> {
    /** 如果 输入/输出 成功了就返回 true */
    boolean success();

    /** 返回输出请求后的物品 */
    List<T> get();
}
