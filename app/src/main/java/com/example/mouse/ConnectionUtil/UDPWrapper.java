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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPWrapper extends NetworkManager{

    public static final int PORT_NUMBER = 12234;

    private String LOG_CLASS = "UDPWrapper";
    static DatagramSocket socket;

    ConcurrentLinkedQueue<byte[]> queue;
    Thread consumer;


    public UDPWrapper(Context context, DataReceivedListener listener) {
        mListener = listener;
        mContext = context;

        queue = new ConcurrentLinkedQueue<>();

        consumer = new Thread() {
            public void run() {
                while (true) {
                    byte[] data = queue.poll();
                    if (data == null || socket == null) {
                        continue;
                    }
                    Log.d(LOG_CLASS, "UDPWrapper : Dispatching data. " + new String(data));
                    try {
                        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(IP_ADDRESS), PORT_NUMBER);
                        socket.send(packet);
                        Log.d(LOG_CLASS, "UDPWrapper : Dispatched Successfully.");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(LOG_CLASS, "UDPWrapper : Dispatch failed");
                    }
                }
            }
        };
        consumer.start();
    }

    public void initSender() {
        try {
            if(socket != null) {
                socket.close();
            }
            socket = new DatagramSocket(PORT_NUMBER);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data) {
        Log.d(LOG_CLASS, "sendData : Adding to queue : \n\t" + new String(data));
        queue.add(data);
    }

    public void sendData(ArrayList<byte[]> data) {
        queue.addAll(data);
    }
}
