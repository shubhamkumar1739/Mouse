package com.example.mouse.ConnectionUtil;

import java.net.DatagramPacket;

public interface UDPDataReceivedListener {
    public void onPacketReceived(DatagramPacket bytes);
}
