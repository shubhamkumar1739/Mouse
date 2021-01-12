package com.example.mouse.ConnectionUtil.WifiPeerUtil;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mouse.R;

public class PeerViewHolder extends RecyclerView.ViewHolder {
    TextView ipView;
    View view;
    public PeerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void bind(Peer peer, int position, PeerClickedListener listener) {
        ipView = view.findViewById(R.id.ip_address);
        ipView.setText(peer.IP_Address);
        ipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPeerClicked(peer);
            }
        });
    }
}
