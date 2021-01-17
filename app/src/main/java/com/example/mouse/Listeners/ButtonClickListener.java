package com.example.mouse.Listeners;

import android.view.View;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.PointerUtils;

public class ButtonClickListener implements View.OnClickListener {

    private NetworkManager networkManager;
    private int mask;
    public ButtonClickListener(NetworkManager nManager, int id) {
        networkManager = nManager;
        mask = id;
    }
    @Override
    public void onClick(View v) {
        String data = System.currentTimeMillis() + "," + PointerUtils.PERFORM_KEY_ACTION + "," + mask;
        networkManager.sendData(data.getBytes());
    }
}
