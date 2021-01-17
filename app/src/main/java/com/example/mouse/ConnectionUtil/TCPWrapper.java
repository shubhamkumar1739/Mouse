package com.example.mouse.ConnectionUtil;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPWrapper extends NetworkManager{

    public static final int PORT_NUMBER = 12237;

    static Socket socket;
    OutputStream outputStream;
    InputStream inputStream;
    PrintWriter printWriter;
    BufferedReader bufferedReader;

    public TCPWrapper(Context context, DataReceivedListener listener) {
        mListener = listener;
    }

    public void initSender() {
        try {
            socket = new Socket(IP_ADDRESS, PORT_NUMBER);

            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);

            inputStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] bytes) {
        String data = new String(bytes);
        Thread t = new Thread() {
            public void run() {
                if(socket == null)
                    initSender();
                printWriter.println(data);
            }
        };
        t.start();
    }
}
