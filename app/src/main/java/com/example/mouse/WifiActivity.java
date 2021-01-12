package com.example.mouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.mouse.ConnectionUtil.UDPDataReceivedListener;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.ConnectionUtil.WifiPeerUtil.Peer;
import com.example.mouse.ConnectionUtil.WifiPeerUtil.PeerClickedListener;
import com.example.mouse.ConnectionUtil.WifiPeerUtil.WifiPeerAdapter;

import java.net.DatagramPacket;
import java.util.ArrayList;

public class WifiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Peer> mList;
    LinearLayoutManager manager;
    WifiPeerAdapter adapter;
    private UDPWrapper mUDPWrapper;
    private UDPDataReceivedListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        initRecyclerView();

        mListener = new UDPDataReceivedListener() {
            @Override
            public void onPacketReceived(DatagramPacket packet) {
                String data = packet.getData().toString();
                String ip = packet.getAddress().getHostAddress();
                Peer peer = new Peer(mList.size(), ip);
                if(!ipMatch(peer, mList)) {
                    mList.add(peer);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        mUDPWrapper = ApplicationContainer.getUDPWrapper(mListener);
    }

    private boolean ipMatch(Peer peer, ArrayList<Peer> mList) {
        for(int i = 0; i < mList.size(); i++) {
            if(peer.IP_Address.equals(mList.get(i).IP_Address)) {
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
                mUDPWrapper.setmIPAddress(peer.IP_Address);
                startActivity(new Intent(WifiActivity.this, MainActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
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
        mUDPWrapper.stopReceiver();
        mUDPWrapper.stopBroadCast();
    }
}