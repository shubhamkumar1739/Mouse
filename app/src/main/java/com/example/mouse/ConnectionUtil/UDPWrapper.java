package com.example.mouse.ConnectionUtil;

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

    private static HashMap<Integer, DatagramSocket> mSocketMap;

    public UDPWrapper(UDPDataReceivedListener listener) {
        mListener = listener;
    }

    static DatagramSocket socket;

    public void sendData(byte[] data) {
        if(socket == null) {
            initSender();
        }
        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT_NUMBER);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Thread receiverThread = new Thread() {
                public void run() {
                    while (isReceiverRunning) {
                        byte buffer[] = new byte[BUFFER_SIZE];
                        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
                        try {
                            receiverSocket.receive(packet);
                            mListener.onPacketReceived(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            receiverThread.start();
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

    public void initBroadCast() {
        try {
            isConnected = false;
            DatagramSocket broadcasterSocket = new DatagramSocket(BROADCASETER_PORT);
            String data = "hello";
            byte[] buffer = data.getBytes();
            InetAddress address = InetAddress.getByName("255.255.255.255");
            broadcasterSocket.setBroadcast(true);

            List<InetAddress> addresses = listAllBroadcastAddresses();

            Thread mThread = new Thread(){
                public void run() {
                    while(!isConnected) {
                        for(InetAddress address : addresses) {
                            if(address == null)
                                continue;
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT_NUMBER);
                            try {
                                broadcasterSocket.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    broadcasterSocket.close();
                }
            };
            mThread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
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
}
