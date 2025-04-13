package com.autoclicker;

public class UserSettings {
    // New properties for duration settings.
    private int toggleDuration; // Maximum time limit in seconds for toggle mode.
    private boolean toggleUnlimited; // If true, toggle mode runs indefinitely.
    private int tapDuration; // Duration in seconds for tap mode (burst duration).

    // New global disable flag.
    private boolean autoClickerDisabled; // If true, no auto clicking occurs.

    private String mode; // "toggle", "hold", or "tap"
    private String startKey; // For toggle mode: key to start (and if same as stopKey, toggles on/off)
    private String stopKey; // For toggle mode: key to stop
    private String holdKey; // For hold mode: key to activate clicking while held down
    private String tapKey; // For tap mode: key to trigger a burst of clicking

    // Default constructor with default settings.
    public UserSettings() {
        // Defaults: 3 seconds for toggle and tap durations.
        this.toggleDuration = 3;
        this.toggleUnlimited = false;
        this.tapDuration = 3;
        this.autoClickerDisabled = false;
        this.mode = "toggle";
        this.startKey = "A";
        // For toggle mode, by default use the same key to toggle on/off.
        this.stopKey = "A";
        this.holdKey = "D";
        this.tapKey = "T";
    }

    // Getters and setters for duration and toggle flag.
    public int getToggleDuration() {
        return toggleDuration;
    }

    public void setToggleDuration(int toggleDuration) {
        this.toggleDuration = toggleDuration;
    }

    public boolean isToggleUnlimited() {
        return toggleUnlimited;
    }

    public void setToggleUnlimited(boolean toggleUnlimited) {
        this.toggleUnlimited = toggleUnlimited;
    }

    public int getTapDuration() {
        return tapDuration;
    }

    public void setTapDuration(int tapDuration) {
        this.tapDuration = tapDuration;
    }

    public boolean isAutoClickerDisabled() {
        return autoClickerDisabled;
    }

    public void setAutoClickerDisabled(boolean autoClickerDisabled) {
        this.autoClickerDisabled = autoClickerDisabled;
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

    public String getTapKey() {
        return tapKey;
    }

    public void setTapKey(String tapKey) {
        this.tapKey = tapKey;
    }
}
