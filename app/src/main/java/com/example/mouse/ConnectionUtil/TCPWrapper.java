package com.example.mouse.ConnectionUtil;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TCPWrapper extends NetworkManager{

    public static final int PORT_NUMBER = 12237;

    static Socket socket;
    OutputStream outputStream;
    PrintWriter printWriter;

    ConcurrentLinkedQueue<byte[]> queue;
    Thread consumer;

    public TCPWrapper(Context context, DataReceivedListener listener) {
        mListener = listener;
        mContext = context;
        queue = new ConcurrentLinkedQueue<>();
        consumer = new Thread() {
            public void run() {
                while (true) {
                    byte[] bytes = queue.poll();
                    if(bytes == null)
                        continue;
                    String data = new String(bytes);
                    printWriter.println(data);
                    printWriter.flush();
                }
            }
        };
        consumer.start();
    }

    public void initSender() {
        try {
            if(socket != null) {
                closeSender();
            }
            socket = new Socket(IP_ADDRESS, PORT_NUMBER);
            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] bytes) {
        queue.add(bytes);
    }

    public void sendData(ArrayList<byte[]> bytes) {
        queue.addAll(bytes);
    }

    public void closeSender() {
        try {
            printWriter.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
