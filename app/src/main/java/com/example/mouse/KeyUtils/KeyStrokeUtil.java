package com.example.mouse.KeyUtils;

import java.util.HashMap;

public class KeyStrokeUtil {
    int keyCode, shift;
    public static HashMap<Character, KeyStrokeUtil> keyMap;
    public KeyStrokeUtil(int keyCode, int shift) {
        this.keyCode = keyCode;
        this.shift = shift;
    }
}
