package com.example.mouse.KeyUtils;

import android.content.Context;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.MainActivity;
import com.example.mouse.PointerUtils;

import java.util.ArrayList;
import java.util.List;

public class KeyboardEvent {
    public static final int BKSP = 8;
    public static final int SPACE = 32;
    public static final int ESC = 27;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int LEFT = 5;
    public static final int RIGHT = 6;
    public static final int INSERT = 7;
    public static final int ENTER = 23;
    public static final int TAB = 9;
    public static final int DELETE = 127;
    public static final int F1 = 11;
    public static final int F2 = 12;
    public static final int F3 = 13;
    public static final int F4 = 14;
    public static final int F5 = 15;
    public static final int F6 = 16;
    public static final int F7 = 17;
    public static final int F8 = 18;
    public static final int F9 = 19;
    public static final int F10 = 20;
    public static final int F11 = 21;
    public static final int F12 = 22;

    public static final int WIN = 128;
    public static final int SHIFT = 130;
    public static final int ALT = 131;
    public static final int CTRL = 132;
    public static final int FN = 133;
    public static final int CAPS = 134;

    public static List typeText(String s, String t, NetworkManager networkManager, MainActivity activity) {
        List list = new ArrayList();

        if(s.length() == 0) {
            activity.addDummy();
        }

        int minLen = Math.min(s.length(), t.length());
        int i, j;
        for(i = 0, j = 0; i < minLen; i++, j++) {
            if (s.charAt(i) != t.charAt(j)) {
                break;
            }
        }

        if(j < t.length() ) {
            String data = CommandUtil.getPerformKeyActionCommand(KeyboardEvent.BKSP, t.length() - j);
            networkManager.sendData(data.getBytes());
            list.add(t.length() - j + " backspaces");
        }

        String diff = s.substring(i);
        String data = CommandUtil.getTextInputCommand(diff);
        networkManager.sendData(data.getBytes());

        activity.setPrevStr(s);

        list.add(diff);

        String logData = System.currentTimeMillis() + "," + PointerUtils.LOG + "," + LogUtil.PREV_CUR_STR  + "," + "\"" + s + "\",\"" + t + "\",\"" + list.toString() + "\"";
        networkManager.sendData(logData.getBytes());

        return list;
    }

    public static boolean isLetterOrDigit(char letter) {
        if(letter >= '0' && letter <= '9')
            return true;
        if(letter >= 'A' && letter <= 'Z')
            return true;
        if(letter >= 'a' && letter <= 'z')
            return true;
        return false;
    }

}
