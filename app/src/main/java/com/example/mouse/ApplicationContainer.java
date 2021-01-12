package com.example.mouse;

import android.app.Application;

import com.example.mouse.ConnectionUtil.UDPDataReceivedListener;
import com.example.mouse.ConnectionUtil.UDPWrapper;

public class ApplicationContainer extends Application {
    public static UDPWrapper mUDPWrapper;

    public static UDPWrapper getUDPWrapper(UDPDataReceivedListener listener) {
        if(mUDPWrapper == null) {
            mUDPWrapper = new UDPWrapper(listener);
        }
        return mUDPWrapper;
    }
}
