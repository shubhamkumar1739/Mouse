package com.example.mouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.mouse.ConnectionUtil.DataReceivedListener;
import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.WifiPeerUtil.Peer;
import com.example.mouse.WifiPeerUtil.PeerClickedListener;
import com.example.mouse.WifiPeerUtil.WifiPeerAdapter;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Peer> mList;
    LinearLayoutManager manager;
    WifiPeerAdapter adapter;
    private NetworkManager networkManager;
    private DataReceivedListener mListener;

    private String LOG_ACTIVITY = "WifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        initRecyclerView();

        mListener = new DataReceivedListener() {
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

        networkManager = ApplicationContainer.getNetworkManager(getApplicationContext(), mListener);
        networkManager.setMainThread(new Handler(Looper.getMainLooper()));
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
                networkManager.setmIPAddress(peer.getmIP_Address());
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
        networkManager.initReceiver();
        networkManager.initBroadCast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mList.clear();
        adapter.notifyDataSetChanged();
        networkManager.stopReceiver();
        networkManager.stopBroadCast();
    }
}