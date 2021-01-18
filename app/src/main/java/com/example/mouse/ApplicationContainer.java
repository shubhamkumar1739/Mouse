package com.example.mouse;

import android.app.Application;
import android.content.Context;

import com.example.mouse.ConnectionUtil.DataReceivedListener;
import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.KeyUtils.KeyStrokeUtil;

import java.util.HashMap;

public class ApplicationContainer extends Application {
    public static NetworkManager networkManager;

    public static NetworkManager getNetworkManager(Context context, DataReceivedListener listener) {
        if(networkManager == null) {
            networkManager = new UDPWrapper(context,listener);
        }
        return networkManager;
    }

    public static HashMap<Character, KeyStrokeUtil> getKeyMap() {
        HashMap<Character, KeyStrokeUtil> keyMap = KeyStrokeUtil.keyMap;
        if(keyMap == null)
            keyMap = new HashMap<>();
        if(keyMap.size() == 0) {
            keyMap.put('!', new KeyStrokeUtil('1', 1));
            keyMap.put('@', new KeyStrokeUtil('2', 1));
            keyMap.put('#', new KeyStrokeUtil('3', 1));
            keyMap.put('$', new KeyStrokeUtil('4', 1));
            keyMap.put('%', new KeyStrokeUtil('5', 1));
            keyMap.put('^', new KeyStrokeUtil('6', 1));
            keyMap.put('&', new KeyStrokeUtil('7', 1));
            keyMap.put('*', new KeyStrokeUtil('8', 1));
            keyMap.put('(', new KeyStrokeUtil('9', 1));
            keyMap.put(')', new KeyStrokeUtil('0', 1));
            keyMap.put('_', new KeyStrokeUtil('-', 1));
            keyMap.put('-', new KeyStrokeUtil('-', 0));
            keyMap.put('+', new KeyStrokeUtil('=', 1));
            keyMap.put('=', new KeyStrokeUtil('=', 0));
            keyMap.put('`', new KeyStrokeUtil(192, 0));
            keyMap.put('~', new KeyStrokeUtil(131, 0));
            keyMap.put('{', new KeyStrokeUtil(161, 0));
            keyMap.put('}', new KeyStrokeUtil(162, 0));
            keyMap.put('[', new KeyStrokeUtil(91, 0));
            keyMap.put(']', new KeyStrokeUtil(93, 0));
            keyMap.put('\\', new KeyStrokeUtil(92, 0));
            keyMap.put('|', new KeyStrokeUtil(92, 1));
            keyMap.put(';', new KeyStrokeUtil(59, 0));
            keyMap.put(':', new KeyStrokeUtil(59, 1));
            keyMap.put('\'', new KeyStrokeUtil(222, 0));
            keyMap.put('\"', new KeyStrokeUtil(222, 1));
            keyMap.put('<', new KeyStrokeUtil(153, 0));
            keyMap.put('>', new KeyStrokeUtil(160, 0));
            keyMap.put(',', new KeyStrokeUtil(44, 0));
            keyMap.put('.', new KeyStrokeUtil(46, 0));
            keyMap.put('/', new KeyStrokeUtil(47, 0));
            keyMap.put('?', new KeyStrokeUtil(47, 1));
            keyMap.put(' ', new KeyStrokeUtil(' ', 0));
        }
        return keyMap;
    }
}
