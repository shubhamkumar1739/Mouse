package com.example.mouse.ConnectionUtil;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.mouse.PointerUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import android.os.Handler;

public class UDPWrapper {
    private static final int BUFFER_SIZE = 16000;
    UDPDataReceivedListener mListener;
    int mUDPPort;
    private InetAddress address;

    public boolean isConnected = false;
    public boolean isReceiverRunning = false;

    public static String IP_ADDRESS;
    public static final int PORT_NUMBER = 12234;
    public static final int BROADCASETER_PORT = 12235;
    public static final int BROADCAST_RECEIVER_PORT = 12236;

    private String LOG_CLASS = "UDPWrapper";
    private Context mContext;

    private Thread receiverThread;
    private Thread broadcasterThread;

    private Handler mainThread;

    private static HashMap<Integer, DatagramSocket> mSocketMap;

    public UDPWrapper(Context context, UDPDataReceivedListener listener) {
        mListener = listener;
        mContext = context;
    }

    static DatagramSocket socket;

    public void sendData(byte[] data) {
        if(socket == null) {
            initSender();
        }
        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT_NUMBER);
        Thread t = new Thread() {
            public void run() {
                try{
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void initSender() {
        try {
            socket = new DatagramSocket(PORT_NUMBER);
            address = InetAddress.getByName(IP_ADDRESS);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void initReceiver() {
        //start stop on pause and resume
        try {
            isReceiverRunning = true;
            DatagramSocket receiverSocket = new DatagramSocket(BROADCAST_RECEIVER_PORT);
            receiverThread = new Thread() {
                public void run() {
                    while (isReceiverRunning) {
                        byte buffer[] = new byte[BUFFER_SIZE];
                        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
                        try {
                            receiverSocket.receive(packet);
                            mListener.onPacketReceived(packet, mainThread);
                            Log.d(LOG_CLASS, "Received packet from : "+ packet.getAddress().getHostAddress());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    receiverThread = null;
                }
            };
            receiverThread.start();
            Log.d(LOG_CLASS, "Receiver setup successful");
            //Toast.makeText(mContext, "Receiver setup successful", Toast.LENGTH_SHORT).show();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void stopReceiver() {
        isReceiverRunning = false;
    }

    public void stopBroadCast() {
        isConnected = true;
    }

    public void setmIPAddress(String ip) {
        IP_ADDRESS = ip;
    }

    int once = 0;
    public void initBroadCast() {
        try {
            isConnected = false;
            DatagramSocket broadcasterSocket = new DatagramSocket(BROADCASETER_PORT);
            String data = System.currentTimeMillis() + "," + PointerUtils.BROADCAST_MESSAGE;
            byte[] buffer = data.getBytes();
            broadcasterSocket.setBroadcast(true);
            List<InetAddress> addresses = listAllBroadcastAddresses();
            if(once == 0) {
                String all_addresses = "";
                once = 1;
                for (InetAddress address : addresses) {
                    if(address == null)
                        continue;
                    String ad = address.getHostAddress();
                    all_addresses += ad + "\n";
                }
                Toast.makeText(mContext, all_addresses, Toast.LENGTH_LONG).show();
            }

            broadcasterThread = new Thread(){
                public void run() {
                    while(!isConnected) {
                        for(InetAddress address : addresses) {
                            if(address == null)
                                continue;
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT_NUMBER);
                            try {
                                broadcasterSocket.send(packet);
                                Log.d(LOG_CLASS, "sent broadcast to : "+ address.getHostAddress());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    broadcasterThread = null;
                    broadcasterSocket.close();
                }
            };
            broadcasterThread.start();
            Log.d(LOG_CLASS, "Broadcaster setup successful");
            //Toast.makeText(mContext, "Broadcaster setup successful", Toast.LENGTH_SHORT).show();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            List<InterfaceAddress> inetAddresses = networkInterface.getInterfaceAddresses();
            for (InterfaceAddress inetAddress : inetAddresses) {
                broadcastList.add(inetAddress.getBroadcast());
            }
        }
        return broadcastList;
    }

    public void setMainThread(Handler h) {
        mainThread = h;
    }
}
