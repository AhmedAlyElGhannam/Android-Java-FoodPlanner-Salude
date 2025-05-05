package com.example.salude.utils.guest;

public class GuestMode {
    private static boolean isGuest;
    static {
        isGuest = false;
    }
    public static void setGuestModeState(boolean stat) {
        isGuest = stat;
    }
    public static boolean getGuestModeState() {
        return isGuest;
    }
}
