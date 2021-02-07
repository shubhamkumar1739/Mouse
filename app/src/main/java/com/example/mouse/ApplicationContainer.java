package com.example.mouse;

import android.app.Application;
import android.content.Context;

import com.example.mouse.ConnectionUtil.DataReceivedListener;
import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.WifiPeerUtil.Peer;

import java.util.HashMap;

public class ApplicationContainer extends Application {
    public static NetworkManager networkManager;
    public static boolean isFreshStrart = true;
    public static Peer selectedPeer;

    public static NetworkManager getNetworkManager(Context context, DataReceivedListener listener) {
        if(networkManager == null) {
            networkManager = new NetworkManager(context,listener);
        }
        return networkManager;
    }
}
