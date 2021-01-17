package com.example.mouse;

import android.app.Application;
import android.content.Context;

import com.example.mouse.ConnectionUtil.DataReceivedListener;
import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;

public class ApplicationContainer extends Application {
    public static NetworkManager networkManager;

    public static NetworkManager getNetworkManager(Context context, DataReceivedListener listener) {
        if(networkManager == null) {
            networkManager = new UDPWrapper(context,listener);
        }
        return networkManager;
    }
}
