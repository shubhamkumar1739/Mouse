package com.example.mouse.Listeners;

import android.view.View;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.MainActivity;
import com.example.mouse.PointerUtils;

public class ButtonClickListener implements View.OnClickListener {

    private NetworkManager networkManager;
    private int mask;
    private MainActivity activity;
    public ButtonClickListener(NetworkManager nManager, int id, MainActivity activity) {
        networkManager = nManager;
        mask = id;
        this.activity = activity;
    }
    @Override
    public void onClick(View v) {
        String data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," + mask;
        networkManager.sendData(data.getBytes(), NetworkManager.UDP_OPTION);
        activity.addDummy();
    }
}
