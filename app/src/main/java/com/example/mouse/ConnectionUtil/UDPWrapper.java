package com.example.mouse.ConnectionUtil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mouse.PointerUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class UDPWrapper extends NetworkManager{

    public static final int PORT_NUMBER = 12234;

    private String LOG_CLASS = "UDPWrapper";
    static DatagramSocket socket;


    public UDPWrapper(Context context, DataReceivedListener listener) {
        mListener = listener;
        mContext = context;
    }

    private void initSender() {
        try {
            socket = new DatagramSocket(PORT_NUMBER);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data) {
        if(socket == null) {
            initSender();
        }
        Thread t = new Thread() {
            public void run() {
                try{
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(IP_ADDRESS), PORT_NUMBER);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
