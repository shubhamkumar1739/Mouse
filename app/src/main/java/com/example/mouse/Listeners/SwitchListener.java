package com.example.mouse.Listeners;

import android.widget.CompoundButton;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.PointerUtils;

public class SwitchListener implements CompoundButton.OnCheckedChangeListener {

    private NetworkManager networkManager;
    private int mask;

    public SwitchListener(NetworkManager nManager, int id) {
        networkManager = nManager;
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
        networkManager.sendData(data.getBytes(), NetworkManager.UDP_OPTION);
    }
}
