package com.notcharrow.interlace.keybinds;

// fixes buttons disappearing on resizing
public interface InterlaceLoomHacks {
    default void interlace$onScreenRefreshed(Runnable runnable) {
    }
}
