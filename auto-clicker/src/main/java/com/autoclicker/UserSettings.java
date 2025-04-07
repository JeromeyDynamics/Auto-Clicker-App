package com.autoclicker;

public class UserSettings {
    private int clickDuration; // Duration in seconds
    private String mode; // "toggle" or "hold"
    private String startKey; // For toggle mode: key to start
    private String stopKey; // For toggle mode: key to stop
    private String holdKey; // For hold mode: key to activate clicking

    // Default constructor with default settings
    public UserSettings() {
        this.clickDuration = 3;
        this.mode = "toggle";
        this.startKey = "A";
        this.stopKey = "S";
        this.holdKey = "D";
    }

    // Getters and setters
    public int getClickDuration() {
        return clickDuration;
    }

    public void setClickDuration(int clickDuration) {
        this.clickDuration = clickDuration;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStartKey() {
        return startKey;
    }

    public void setStartKey(String startKey) {
        this.startKey = startKey;
    }

    public String getStopKey() {
        return stopKey;
    }

    public void setStopKey(String stopKey) {
        this.stopKey = stopKey;
    }

    public String getHoldKey() {
        return holdKey;
    }

    public void setHoldKey(String holdKey) {
        this.holdKey = holdKey;
    }
}
