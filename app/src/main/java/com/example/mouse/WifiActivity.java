package com.example.mouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.mouse.ConnectionUtil.UDPDataReceivedListener;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.WifiPeerUtil.Peer;
import com.example.mouse.WifiPeerUtil.PeerClickedListener;
import com.example.mouse.WifiPeerUtil.WifiPeerAdapter;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class WifiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Peer> mList;
    LinearLayoutManager manager;
    WifiPeerAdapter adapter;
    private UDPWrapper mUDPWrapper;
    private UDPDataReceivedListener mListener;

    private String LOG_ACTIVITY = "WifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        initRecyclerView();

        mListener = new UDPDataReceivedListener() {
            @Override
            public void onPacketReceived(DatagramPacket packet, Handler mainThread) {
                String ip = packet.getAddress().getHostAddress();
                Peer peer = new Peer(ip, new String(packet.getData()));
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!ipMatch(peer, mList)) {
                            //Toast.makeText(WifiActivity.this, "Packet Received from : " + ip, Toast.LENGTH_SHORT).show();
                            mList.add(peer);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        };

        mUDPWrapper = ApplicationContainer.getUDPWrapper(getApplicationContext(), mListener);
        mUDPWrapper.setMainThread(new Handler(Looper.getMainLooper()));
    }

    private boolean ipMatch(Peer peer, List<Peer> mList) {
        for(int i = 0; i < mList.size(); i++) {
            if(peer.getmIP_Address().equals(mList.get(i).getmIP_Address())) {
                return true;
            }
        }
        return false;
    }

    private void initRecyclerView() {
        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(this);
        adapter = new WifiPeerAdapter(this, mList, new PeerClickedListener() {
            @Override
            public void onPeerClicked(Peer peer) {
                mUDPWrapper.setmIPAddress(peer.getmIP_Address());
                startActivity(new Intent(WifiActivity.this, MainActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        Log.d(LOG_ACTIVITY, "Recycler view setup successful");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUDPWrapper.initReceiver();
        mUDPWrapper.initBroadCast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mList.clear();
        adapter.notifyDataSetChanged();
        mUDPWrapper.stopReceiver();
        mUDPWrapper.stopBroadCast();
    }
}