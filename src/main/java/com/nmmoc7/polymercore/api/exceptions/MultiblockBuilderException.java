package com.nmmoc7.polymercore.api.exceptions;

public class MultiblockBuilderException extends RuntimeException {
    public MultiblockBuilderException(String message) {
        super(message);
    }

    public MultiblockBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
