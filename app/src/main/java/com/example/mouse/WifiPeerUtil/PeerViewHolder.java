package com.example.mouse.WifiPeerUtil;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mouse.R;

public class PeerViewHolder extends RecyclerView.ViewHolder {
    TextView ipView;
    TextView nameView;
    TextView osNameView;
    TextView osVersionView;
    ImageView icon;
    View view;
    public PeerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        ipView = view.findViewById(R.id.ip_address);
        nameView = view.findViewById(R.id.name_view);
        osNameView = view.findViewById(R.id.os_name_view);
        osVersionView = view.findViewById(R.id.os_version_view);
        icon = view.findViewById(R.id.os_icon_view);
    }

    public void bind(Peer peer, PeerClickedListener listener) {
        ipView.setText("IP : " + peer.getmIP_Address());
        nameView.setText("Name : " +peer.getmUsername());
        osNameView.setText("OS : " + peer.getmOSName());
        osVersionView.setText("OS Version : " + peer.getmOSVersion());

        String osName = peer.getmOSName();

        if(osName.equals("Linux")) {
            icon.setImageResource(R.drawable.linux_icon);
        } else if(osName.contains("Windows")) {
            icon.setImageResource(R.drawable.windows);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPeerClicked(peer);
            }
        });
    }
}
