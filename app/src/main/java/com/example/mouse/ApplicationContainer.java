package com.example.mouse;

import android.app.Application;
import android.content.Context;

import com.example.mouse.ConnectionUtil.UDPDataReceivedListener;
import com.example.mouse.ConnectionUtil.UDPWrapper;

public class ApplicationContainer extends Application {
    public static UDPWrapper mUDPWrapper;

    public static UDPWrapper getUDPWrapper(Context context, UDPDataReceivedListener listener) {
        if(mUDPWrapper == null) {
            mUDPWrapper = new UDPWrapper(context,listener);
        }
        return mUDPWrapper;
    }
}
