package com.example.mouse.Listeners;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.example.mouse.ConnectionUtil.NetworkManager;
import com.example.mouse.ConnectionUtil.UDPDataReceivedListener;
import com.example.mouse.ConnectionUtil.UDPWrapper;
import com.example.mouse.PointerUtils;

public class MouseTouchListener implements View.OnTouchListener {

    private VelocityTracker velocityTracker;
    UDPWrapper mUDPWrapper;
    private boolean isDragEvent;
    private boolean isRightClickEvent;
    private boolean isCursorMoved;

    private long lastDownEvent;
    private boolean isSecondPointerDown;

    public MouseTouchListener(UDPWrapper udpWrapper) {
        velocityTracker = null;

        lastDownEvent = 0;
        isDragEvent = false;
        mUDPWrapper = udpWrapper;

    }

    private void logPointerUp() {
        isSecondPointerDown = false;
        isRightClickEvent = true;
    }

    private void logPointerDown() {
        isSecondPointerDown = true;
    }

    private void logMoveEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        velocityTracker.computeCurrentVelocity(1);
        sendVelocityPacket(velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
        isCursorMoved = true;
    }

    private void logDownEvent() {
        long cur = System.currentTimeMillis();
        if(cur - lastDownEvent <= PointerUtils.DRAG_START_DELAY) {
            isDragEvent = true;
            sendPressPacket();
        }
        lastDownEvent = System.currentTimeMillis();
        isCursorMoved = false;
    }

    private void logUpEvent() {
        long curTime = System.currentTimeMillis();
        if(isDragEvent) {
            isDragEvent = false;
            sendReleasePacket();
        } else if (isRightClickEvent && !isCursorMoved && curTime - lastDownEvent < PointerUtils.CLICK_DELAY) {
            sendRightClickPacket();
        } else if(!isCursorMoved && curTime - lastDownEvent < PointerUtils.CLICK_DELAY) {
            sendClickPacket();
        }
        isRightClickEvent = false;
    }

    private void sendRightClickPacket() {
        String data = System.currentTimeMillis() + "," + PointerUtils.MOUSE_RIGHT_CLICK;
        mUDPWrapper.sendData(data.getBytes());
    }

    private void sendReleasePacket() {
        String data = System.currentTimeMillis() + "," + PointerUtils.LEFT_BUTTON_RELEASE;
        mUDPWrapper.sendData(data.getBytes());
    }

    private void sendPressPacket() {
        String data = System.currentTimeMillis() + "," + PointerUtils.LEFT_BUTTON_PRESS;
        mUDPWrapper.sendData(data.getBytes());
    }

    public void sendClickPacket() {
        String data = System.currentTimeMillis() + "," + PointerUtils.MOUSE_CLICK;
        mUDPWrapper.sendData(data.getBytes());
    }

    private void sendVelocityPacket(float xVelocity, float yVelocity) {
        int scale = PointerUtils.getScale(xVelocity, yVelocity);
        long timestamp = System.currentTimeMillis();

        String data;
        if (!isSecondPointerDown)
            data = timestamp + "," + PointerUtils.MOUSE_MOVE + "," + xVelocity + "," + yVelocity + "," + scale;
        else
            data = timestamp + "," + PointerUtils.MOUSE_SCROLL + "," + xVelocity + "," + yVelocity + "," + PointerUtils.SCROLL_TYPE;
        mUDPWrapper.sendData(data.getBytes());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        switch(action) {
            case MotionEvent.ACTION_DOWN :
                if(velocityTracker == null)
                    velocityTracker = VelocityTracker.obtain();
                else
                    velocityTracker.clear();
                logDownEvent();
                break;
            case MotionEvent.ACTION_MOVE:
                logMoveEvent(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                logPointerDown();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                logPointerUp();
                break;
            case MotionEvent.ACTION_UP:
                logUpEvent();
                //velocityTracker.recycle();
                break;
            case MotionEvent.ACTION_CANCEL:
                //velocityTracker.recycle();
                break;

        }
        return true;
    }
}
