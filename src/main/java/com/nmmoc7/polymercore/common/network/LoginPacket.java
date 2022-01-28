package com.nmmoc7.polymercore.common.network;

import java.util.function.IntSupplier;

public class LoginPacket implements IntSupplier {
    private int loginIndex;

    void setLoginIndex(final int loginIndex) {
        this.loginIndex = loginIndex;
    }

    int getLoginIndex() {
        return loginIndex;
    }

    @Override
    public int getAsInt() {
        return getLoginIndex();
    }


}
