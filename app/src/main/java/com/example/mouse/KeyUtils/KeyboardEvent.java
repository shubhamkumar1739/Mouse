package com.example.mouse.KeyUtils;

import android.content.Context;

import com.example.mouse.ApplicationContainer;
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


    private static int MAX_LENGTH = 10;

    public static List typeText(String s, String t, NetworkManager networkManager, MainActivity activity) {
        List list = new ArrayList();

        //if the user just presses backspace
        if(s.length() == 0) {
            String data = CommandUtil.getPerformKeyActionCommand(KeyboardEvent.BKSP);
            networkManager.sendData(data.getBytes(), NetworkManager.UDP_OPTION);
            activity.addDummy();
        }

        //find the point of difference
        int minLen = Math.min(s.length(), t.length());
        int i, j;
        for(i = 0, j = 0; i < minLen; i++, j++) {
            if (s.charAt(i) != t.charAt(j)) {
                break;
            }
        }

        //delete unmatching text from previous string
        if(j < t.length() ) {
            String data = CommandUtil.getPerformKeyActionCommand(KeyboardEvent.BKSP, t.length() - j);
            networkManager.sendData(data.getBytes(), NetworkManager.UDP_OPTION);
            list.add(t.length() - j + " backspaces");
        }

        String diff = s.substring(i);

        //send new string from diff
        sendData(diff, networkManager, activity);

        activity.setPrevStr(s);

        list.add(diff);

        String logData = System.currentTimeMillis() + "," + PointerUtils.LOG + "," + LogUtil.PREV_CUR_STR  + "," + "\"" + s + "\",\"" + t + "\",\"" + list.toString() + "\"";
        networkManager.sendData(logData.getBytes(), NetworkManager.UDP_OPTION);

        return list;
    }

    public static void sendData(String data, NetworkManager networkManager, MainActivity activity) {
        String dataItem;

        if(data.length() == 0)
            return;

        //splits into individual word tokens
        String components[] = data.split(" ");

        for (int i = 0; i < components.length; i++) {
            String str = components[i];
            if (!str.equals("")) {
                dataItem = CommandUtil.getTextInputCommand(str);
                networkManager.sendData(dataItem.getBytes(), NetworkManager.UDP_OPTION);
            }

            if (i != components.length - 1) {
                dataItem = CommandUtil.getPerformKeyActionCommand(KeyboardEvent.SPACE);
                networkManager.sendData(dataItem.getBytes(), NetworkManager.UDP_OPTION);
            }
        }

        if (data.length() > 0 && data.charAt(data.length() - 1) == ' ') {
            dataItem = CommandUtil.getPerformKeyActionCommand(KeyboardEvent.SPACE);
            networkManager.sendData(dataItem.getBytes(), NetworkManager.UDP_OPTION);
        }

        if(components.length > MAX_LENGTH) {
            activity.addDummy();
        }
    }

}
