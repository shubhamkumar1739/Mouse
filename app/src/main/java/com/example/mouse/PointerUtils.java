package com.example.mouse;

public class PointerUtils {

    public static final int NORMAL_SCROLL = -1;
    public static final int INVERTED_SCROLL = 1;

    public static final int MOUSE_MOVE = 1;
    public static final int MOUSE_CLICK = 2;
    public static final int LEFT_BUTTON_PRESS = 3;
    public static final int LEFT_BUTTON_RELEASE = 4;
    public static final int MOUSE_SCROLL = 5;
    public static final int MOUSE_RIGHT_CLICK = 6;
    public static final int RIGHT_BUTTON_PRESS = 7;
    public static final int RIGHT_BUTTON_RELEASE = 8;
    public static final int KEY_PRESSED = 9;
    public static final int KEY_RELEASED = 10;
    public static final int TEXT_INPUT = 11;
    public static final int PERFORM_KEY_ACTION = 12;

    public static long CLICK_DELAY = 300;
    public static long DRAG_START_DELAY = 300;
    public static int SCROLL_TYPE = NORMAL_SCROLL;

    public static final int LEFT_BUTTON_MASK = 1;
    public static final int RIGHT_BUTTON_MASK = 2;


    public static void setClickDelay(long clickDelay) {
        CLICK_DELAY = clickDelay;
    }

    public static int getScale(float xVelocity, float yVelocity) {
        int scale = 0;
        double sum = xVelocity * xVelocity + yVelocity * yVelocity;
        double velocity = Math.sqrt(sum);
        scale = Math.min((int) Math.round(velocity * 10), 12) + 3;
        return scale;
    }
}
