package com.example.mouse.Listeners;

import android.widget.CompoundButton;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.MainActivity;
import com.example.mouse.PointerUtils;

public class SwitchListener implements CompoundButton.OnCheckedChangeListener {

    private NetworkManager networkManager;
    private int mask;
    private MainActivity activity;

    public SwitchListener(NetworkManager nManager, int id, MainActivity activity) {
        networkManager = nManager;
        mask = id;
        this.activity = activity;
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
        activity.addDummy();
    }
}
