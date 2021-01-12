package com.example.mouse.Listeners;

import android.widget.CompoundButton;

import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.PointerUtils;

public class SwitchListener implements CompoundButton.OnCheckedChangeListener {

    private UDPWrapper mUDPWrapper;
    private int mask;

    public SwitchListener(UDPWrapper udpWrapper, int id) {
        mUDPWrapper = udpWrapper;
        mask = id;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String data;
        if(isChecked) {
            data = System.currentTimeMillis() + "," + PointerUtils.KEY_PRESSED + "," + mask;
        } else {
            data = System.currentTimeMillis() + "," + PointerUtils.KEY_RELEASED + "," + mask;
        }
        mUDPWrapper.sendData(data.getBytes());
    }
}
