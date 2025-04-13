package com.autoclicker;

public class UserSettings {
    // Duration properties (in seconds)
    private int toggleDuration; // Maximum time limit for toggle mode
    private boolean toggleUnlimited; // If true, toggle mode runs indefinitely
    private int tapDuration; // Duration for tap mode (burst duration)

    // Speed properties for each mode (scale 1-10)
    private int toggleSpeed;
    private int holdSpeed;
    private int tapSpeed;

    // Global disable flag.
    private boolean autoClickerDisabled;

    // Global flag for requiring a double key press.
    private boolean requireDoubleKey;

    // Mode and keys
    private String mode; // "toggle", "hold", or "tap"

    // Primary keys.
    private String startKey; // For toggle mode (activation)
    private String stopKey; // For toggle mode (deactivation)
    private String holdKey; // For hold mode
    private String tapKey; // For tap mode

    // Secondary keys (used only if requireDoubleKey is true).
    private String secondaryStartKey;
    private String secondaryHoldKey;
    private String secondaryTapKey;

    // Default constructor with default settings.
    public UserSettings() {
        // Default durations (in seconds)
        this.toggleDuration = 3;
        this.toggleUnlimited = false;
        this.tapDuration = 3;
        // Default speeds (scale 1 to 10)
        this.toggleSpeed = 5;
        this.holdSpeed = 5;
        this.tapSpeed = 5;
        // Auto clicker is enabled by default.
        this.autoClickerDisabled = false;
        // By default, do not require double key press.
        this.requireDoubleKey = false;
        // Default mode and keys.
        this.mode = "toggle";
        this.startKey = "A";
        this.stopKey = "A"; // Same as startKey by default for toggling.
        this.holdKey = "D";
        this.tapKey = "T";
        // Default secondary keys
        this.secondaryStartKey = "B";
        this.secondaryHoldKey = "F";
        this.secondaryTapKey = "Y";
    }

    // Getters and setters for durations
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

    // Getters and setters for speeds
    public int getToggleSpeed() {
        return toggleSpeed;
    }

    public void setToggleSpeed(int toggleSpeed) {
        this.toggleSpeed = toggleSpeed;
    }

    public int getHoldSpeed() {
        return holdSpeed;
    }

    public void setHoldSpeed(int holdSpeed) {
        this.holdSpeed = holdSpeed;
    }

    public int getTapSpeed() {
        return tapSpeed;
    }

    public void setTapSpeed(int tapSpeed) {
        this.tapSpeed = tapSpeed;
    }

    // Getters and setters for disable flag.
    public boolean isAutoClickerDisabled() {
        return autoClickerDisabled;
    }

    public void setAutoClickerDisabled(boolean autoClickerDisabled) {
        this.autoClickerDisabled = autoClickerDisabled;
    }

    // Getter and setter for require double key flag.
    public boolean isRequireDoubleKey() {
        return requireDoubleKey;
    }

    public void setRequireDoubleKey(boolean requireDoubleKey) {
        this.requireDoubleKey = requireDoubleKey;
    }

    // Getters and setters for mode and primary keys.
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

    // Getters and setters for secondary keys.
    public String getSecondaryStartKey() {
        return secondaryStartKey;
    }

    public void setSecondaryStartKey(String secondaryStartKey) {
        this.secondaryStartKey = secondaryStartKey;
    }

    public String getSecondaryHoldKey() {
        return secondaryHoldKey;
    }

    public void setSecondaryHoldKey(String secondaryHoldKey) {
        this.secondaryHoldKey = secondaryHoldKey;
    }

    public String getSecondaryTapKey() {
        return secondaryTapKey;
    }

    public void setSecondaryTapKey(String secondaryTapKey) {
        this.secondaryTapKey = secondaryTapKey;
    }
}
