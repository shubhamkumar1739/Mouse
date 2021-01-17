package com.example.mouse.WifiPeerUtil;

public class Peer {
    public String mIP_Address;
    public String mUsername;
    public String mOSName;

    public String getmIP_Address() {
        return mIP_Address;
    }

    public void setmIP_Address(String mIP_Address) {
        this.mIP_Address = mIP_Address;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmOSName() {
        return mOSName;
    }

    public void setmOSName(String mOSName) {
        this.mOSName = mOSName;
    }

    public String getmOSVersion() {
        return mOSVersion;
    }

    public void setmOSVersion(String mOSVersion) {
        this.mOSVersion = mOSVersion;
    }

    public String mOSVersion;
    public Peer(String ip, String data) {
        String comps[] = data.trim().split(",");
        mIP_Address = ip;
        mUsername = comps[2];
        mOSName = comps[3];
        mOSVersion = comps[4];
    }
}
