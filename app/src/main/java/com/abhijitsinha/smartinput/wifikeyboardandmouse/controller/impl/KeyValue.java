package com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.impl;

import android.view.KeyEvent;

import com.abhijitsinha.smartinput.wifikeyboardandmouse.controller.CommandValue;

public class KeyValue implements CommandValue {
    private final int keyCode;
    private boolean isShiftPressed;
    private boolean isAltPressed;
    private boolean isCtrlPressed;
    private boolean isNumLockPressed;
    private boolean isScrollLockPressed;
    private boolean isFunctionPressed;
    private boolean isMetaPressed;
    private boolean isSymPressed;
    private boolean isCapsLockPressed;
    private boolean isPrintKeyPressed;

    private KeyValue(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isAltPressed() {
        return isAltPressed;
    }

    public boolean isCtrlPressed() {
        return isCtrlPressed;
    }

    public boolean isCapsLockPressed() {
        return isCapsLockPressed;
    }

    public boolean isFunctionPressed() {
        return isFunctionPressed;
    }

    public boolean isMetaPressed() {
        return isMetaPressed;
    }

    public boolean isNumLockPressed() {
        return isNumLockPressed;
    }

    public boolean isPrintKeyPressed() {
        return isPrintKeyPressed;
    }

    public boolean isScrollLockPressed() {
        return isScrollLockPressed;
    }

    public boolean isShiftPressed() {
        return isShiftPressed;
    }

    public boolean isSymPressed() {
        return isSymPressed;
    }

    public static KeyValue of(KeyEvent event) {
        int keyChar = event.getUnicodeChar();
        KeyValue value = new KeyValue(keyChar);
        if (keyChar == 0) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DEL: {
                }
                break;
                case KeyEvent.KEYCODE_FORWARD_DEL: {
                }
                break;
                case KeyEvent.KEYCODE_3: {
                }
                break;
                case KeyEvent.KEYCODE_8: {
                }
                break;
                case KeyEvent.KEYCODE_9: {
                }
                break;
            }
        }
        value.isAltPressed = event.isAltPressed();
        value.isCapsLockPressed = event.isCapsLockOn();
        value.isCtrlPressed = event.isCtrlPressed();
        value.isFunctionPressed = event.isFunctionPressed();
        value.isMetaPressed = event.isMetaPressed();
        value.isNumLockPressed = event.isNumLockOn();
        value.isPrintKeyPressed = event.isPrintingKey();
        value.isScrollLockPressed = event.isScrollLockOn();
        value.isShiftPressed = event.isShiftPressed();
        value.isSymPressed = event.isSymPressed();
        return value;
    }

    @Override
    public String toString() {
        return String.format("{\"keyCode\": %s, \"alt\": %s, \"caps\": %s, \"ctrl\": %s, \"fn\": %s, \"meta\": %s, \"num\": %s, \"print\": %s, \"scroll\": %s, \"shift\": %s, \"sym\": %s}",
                getKeyCode(), isAltPressed(), isCapsLockPressed(), isCtrlPressed(), isFunctionPressed(), isMetaPressed(), isNumLockPressed(), isPrintKeyPressed(), isScrollLockPressed(), isShiftPressed(), isSymPressed());
    }
}
