package com.example.mouse.Listeners;

import android.view.View;

import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.PointerUtils;

public class ButtonClickListener implements View.OnClickListener {

    private UDPWrapper mUDPWrapper;
    private int mask;
    public ButtonClickListener(UDPWrapper udpWrapper, int id) {
        mUDPWrapper = udpWrapper;
        mask = id;
    }
    @Override
    public void onClick(View v) {
        String data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," + mask;
        mUDPWrapper.sendData(data.getBytes());
    }
}
