package com.example.mouse.ConnectionUtil.WifiPeerUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mouse.R;

import java.util.ArrayList;

public class WifiPeerAdapter extends RecyclerView.Adapter {

    ArrayList<Peer> mList;
    Context mContext;
    PeerClickedListener mListener;

    public WifiPeerAdapter(Context context, ArrayList<Peer> list, PeerClickedListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.peer_view, null);
        return new PeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PeerViewHolder) holder).bind(mList.get(position), position, mListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
