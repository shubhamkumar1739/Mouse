package com.example.mouse.ConnectionUtil;

import android.content.Context;
import android.os.Handler;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static com.example.mouse.ConnectionUtil.UDPWrapper.PORT_NUMBER;

public abstract class NetworkManager {
    static final int BUFFER_SIZE = 16000;

    public static final int BROADCASTER_PORT = 12235;
    public static final int BROADCAST_RECEIVER_PORT = 12236;

    public static final int TCP_OPTION = 0;
    public static final int UDP_OPTION = 1;

    DataReceivedListener mListener;

    public boolean isConnected = false;
    public boolean isReceiverRunning = false;

    UDPWrapper udpWrapper;
    TCPWrapper tcpWrapper;

    public static String IP_ADDRESS;

    Thread receiverThread;
    Thread broadcasterThread;

    Handler mainThread;

    Context mContext;

    public NetworkManager() {};

    public NetworkManager(Context context, DataReceivedListener listener) {
        mContext = context;
        mListener = listener;
        tcpWrapper = new TCPWrapper(mContext, mListener);
        udpWrapper = new UDPWrapper(mContext, mListener);
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
                            //Log.d(LOG_CLASS, "Received packet from : "+ packet.getAddress().getHostAddress());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    receiverThread = null;
                }
            };
            receiverThread.start();
            //Log.d(LOG_CLASS, "Receiver setup successful");
            //Toast.makeText(mContext, "Receiver setup successful", Toast.LENGTH_SHORT).show();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void initBroadCast() {
        try {
            isConnected = false;
            DatagramSocket broadcasterSocket = new DatagramSocket(BROADCASTER_PORT);
            String data = System.currentTimeMillis() + "," + PointerUtils.BROADCAST_MESSAGE;
            byte[] buffer = data.getBytes();
            broadcasterSocket.setBroadcast(true);
            List<InetAddress> addresses = listAllBroadcastAddresses();
            String all_addresses = "";
            for (InetAddress address : addresses) {
                if(address == null)
                    continue;
                String ad = address.getHostAddress();
                all_addresses += ad + "\n";
            }
            Toast.makeText(mContext, all_addresses, Toast.LENGTH_LONG).show();

            broadcasterThread = new Thread(){
                public void run() {
                    while(!isConnected) {
                        for(InetAddress address : addresses) {
                            if(address == null)
                                continue;
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, BROADCASTER_PORT);
                            try {
                                broadcasterSocket.send(packet);
                                //Log.d(LOG_CLASS, "sent broadcast to : "+ address.getHostAddress());
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
            //Log.d(LOG_CLASS, "Broadcaster setup successful");
            //Toast.makeText(mContext, "Broadcaster setup successful", Toast.LENGTH_SHORT).show();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setmIPAddress(String ip) {
        IP_ADDRESS = ip;
    }

    public void setMainThread(Handler h) {
        mainThread = h;
    }

    public void stopReceiver() {
        isReceiverRunning = false;
    }

    public void stopBroadCast() {
        isConnected = true;
    }

    public void sendData(byte[] data, int option) {
        if(option == UDP_OPTION) {
            udpWrapper.sendData(data);
        } else if(option == TCP_OPTION) {
            tcpWrapper.sendData(data);
        }
    }

    //TODO : Remove this function
    public void sendData(byte[] data) {
        udpWrapper.sendData(data);
    }

}

