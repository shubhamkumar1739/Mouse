package com.example.mouse.KeyUtils;

import com.example.mouse.PointerUtils;

import java.util.Optional;

public class CommandUtil {
    public static String getPerformKeyActionCommand(Integer keyCode, Integer keyPresses) {
        String command = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION;
        if(keyCode != null) {
            command += "," + keyCode;
        }
        if(keyPresses == null) {
            keyPresses = 1;
        }
        command += "," + keyPresses;
        return command;
    }

    public static String getPerformKeyActionCommand(Integer keyCode) {
        return getPerformKeyActionCommand(keyCode, null);
    }

    public static String getTextInputCommand(String text) {
        String command = System.currentTimeMillis() + "," +PointerUtils.TEXT_INPUT + ",\"" +text +"\"";
        return command;
    }

    public static String getKeyPhysicalActionCommand(KeyStrokeUtil keyStrokeUtil) {
        String command = System.currentTimeMillis() + "," + PointerUtils.PHYSICAL_KEY_ACTION + "," + keyStrokeUtil.keyCode + "," + keyStrokeUtil.shift;
        return command;
    }
}
