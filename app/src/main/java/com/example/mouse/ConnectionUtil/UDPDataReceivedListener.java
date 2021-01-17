package com.example.mouse.ConnectionUtil;

import android.os.Handler;

import java.net.DatagramPacket;

public interface UDPDataReceivedListener {
    public void onPacketReceived(DatagramPacket bytes, Handler maintThread);
}
