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

    /**
     * Gets the maximum time limit for toggle mode in seconds.
     * 
     * @return the maximum toggle duration in seconds
     */
    public int getToggleDuration() {
        return toggleDuration;
    }

    /**
     * Sets the maximum time limit for toggle mode (in seconds). If this is set to 0
     * or negative, toggle mode will not have a time limit.
     * 
     * @param toggleDuration the maximum time limit (in seconds) for toggle mode
     */
    public void setToggleDuration(int toggleDuration) {
        this.toggleDuration = toggleDuration;
    }

    /**
     * Returns true if toggle mode is set to unlimited duration, false
     * otherwise. If this is true, the toggle mode will not have a time
     * limit, and the toggle duration setting will be ignored.
     * 
     * @return true if toggle mode is set to unlimited duration, false
     *         otherwise
     */
    public boolean isToggleUnlimited() {
        return toggleUnlimited;
    }

    /**
     * Sets whether toggle mode should be unlimited duration. If this is true, the
     * toggle mode will not have a time limit, and the toggle duration setting will
     * be ignored.
     * 
     * @param toggleUnlimited true if toggle mode should be unlimited duration,
     *                        false otherwise
     */
    public void setToggleUnlimited(boolean toggleUnlimited) {
        this.toggleUnlimited = toggleUnlimited;
    }

    /**
     * Returns the duration of the tap mode (in seconds). This controls how long
     * the auto-clicker will click for when the tap key is pressed.
     * 
     * @return the duration of the tap mode (in seconds)
     */
    public int getTapDuration() {
        return tapDuration;
    }

    /**
     * Sets the duration of the tap mode (in seconds). This determines how long
     * the auto-clicker will click when the tap key is pressed.
     * 
     * @param tapDuration the duration for tap mode in seconds
     */
    public void setTapDuration(int tapDuration) {
        this.tapDuration = tapDuration;
    }

    /**
     * Returns the speed of toggle mode. This controls how fast the auto-clicker
     * clicks when the toggle mode is enabled. The speed is a value from 1-10, with
     * 1 being the fastest and 10 being the slowest.
     * 
     * @return the speed of toggle mode from 1-10
     */
    public int getToggleSpeed() {
        return toggleSpeed;
    }

    /**
     * Sets the speed of toggle mode. This controls how fast the auto-clicker
     * clicks when the toggle mode is enabled. The speed is a value from 1-10, with
     * 1 being the fastest and 10 being the slowest.
     * 
     * @param toggleSpeed the speed of toggle mode from 1-10
     */
    public void setToggleSpeed(int toggleSpeed) {
        this.toggleSpeed = toggleSpeed;
    }

    /**
     * Returns the speed of the hold mode. This controls how fast the auto-clicker
     * clicks when the hold key is pressed. The speed is a value from 1-10, with
     * 1 being the fastest and 10 being the slowest.
     * 
     * @return the speed of the hold mode from 1-10
     */
    public int getHoldSpeed() {
        return holdSpeed;
    }

    /**
     * Sets the speed of the hold mode. This controls how fast the auto-clicker
     * clicks when the hold key is pressed. The speed is a value from 1-10, with
     * 1 being the fastest and 10 being the slowest.
     * 
     * @param holdSpeed the speed of the hold mode from 1-10
     */
    public void setHoldSpeed(int holdSpeed) {
        this.holdSpeed = holdSpeed;
    }

    /**
     * Returns the speed of the tap mode. This controls how fast the auto-clicker
     * clicks when the tap key is pressed. The speed is a value from 1-10, with
     * 1 being the fastest and 10 being the slowest.
     * 
     * @return the speed of the tap mode from 1-10
     */
    public int getTapSpeed() {
        return tapSpeed;
    }

    /**
     * Sets the speed of the tap mode. This controls how fast the auto-clicker
     * clicks when the tap key is pressed. The speed is a value from 1-10, with
     * 1 being the fastest and 10 being the slowest.
     * 
     * @param tapSpeed the speed of the tap mode from 1-10
     */
    public void setTapSpeed(int tapSpeed) {
        this.tapSpeed = tapSpeed;
    }

    /**
     * Returns true if the auto-clicker is disabled, false otherwise.
     * When the auto-clicker is disabled, it will not respond to key events
     * and will not perform any auto-clicking actions.
     * 
     * @return true if the auto-clicker is disabled, false otherwise
     */
    public boolean isAutoClickerDisabled() {
        return autoClickerDisabled;
    }

    /**
     * Sets the flag that indicates whether the auto-clicker should be disabled or
     * not. If this flag is set to true, the auto-clicker will not respond to any
     * key events and will not perform any auto-clicking, even if the settings
     * window is not focused.
     * 
     * @param autoClickerDisabled true to disable the auto-clicker, false to
     *                            enable it
     */
    public void setAutoClickerDisabled(boolean autoClickerDisabled) {
        this.autoClickerDisabled = autoClickerDisabled;
    }

    /**
     * Returns true if the auto-clicker requires the secondary key to be pressed
     * before it will start clicking, false otherwise.
     * 
     * @return true if double key is required, false if double key is not required
     */
    public boolean isRequireDoubleKey() {
        return requireDoubleKey;
    }

    /**
     * Sets the flag that indicates whether the auto-clicker should require the
     * secondary key to be pressed before the auto-clicker will start clicking.
     * If this flag is set to true, the auto-clicker will only start clicking when
     * both the primary and secondary keys are pressed. If this flag is set to
     * false, the auto-clicker will start clicking as soon as the primary key is
     * pressed.
     * 
     * @param requireDoubleKey true to require double key, false to not require
     *                         double key
     */
    public void setRequireDoubleKey(boolean requireDoubleKey) {
        this.requireDoubleKey = requireDoubleKey;
    }

    /**
     * Gets the mode for the auto-clicker. This mode determines the behavior
     * of the auto-clicker, such as toggle, hold, or tap mode.
     * 
     * @return the mode of the auto-clicker
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the mode for the auto-clicker. This mode determines the behavior
     * of the auto-clicker, such as toggle, hold, or tap mode.
     *
     * @param mode the mode to set for the auto-clicker
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Retrieves the start key for the auto-clicker. This key is used to
     * activate or initiate the auto-clicking process, depending on the
     * current mode and settings.
     *
     * @return the start key as a String
     */
    public String getStartKey() {
        return startKey;
    }

    /**
     * Sets the start key for the auto-clicker. This key is used to
     * activate or initiate the auto-clicking process, depending on the
     * current mode and settings.
     *
     * @param startKey the key to be set as the start key for the auto-clicker
     */
    public void setStartKey(String startKey) {
        this.startKey = startKey;
    }

    /**
     * Retrieves the stop key for the auto-clicker. This key is used to
     * stop or deactivate the auto-clicking process, regardless of the
     * current mode and settings.
     *
     * @return the stop key as a String
     */
    public String getStopKey() {
        return stopKey;
    }

    /**
     * Sets the stop key for the auto-clicker. This key is used to
     * stop or deactivate the auto-clicking process, regardless of the
     * current mode and settings.
     *
     * @param stopKey the key to be set as the stop key for the auto-clicker
     */
    public void setStopKey(String stopKey) {
        this.stopKey = stopKey;
    }

    /**
     * Retrieves the hold key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in hold mode.
     * 
     * @return the hold key as a String
     */
    public String getHoldKey() {
        return holdKey;
    }

    /**
     * Sets the hold key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in hold mode.
     * 
     * @param holdKey the key to be set as the hold key for the auto-clicker
     */
    public void setHoldKey(String holdKey) {
        this.holdKey = holdKey;
    }

    /**
     * Retrieves the tap key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in tap mode.
     * 
     * @return the tap key as a String
     */
    public String getTapKey() {
        return tapKey;
    }

    /**
     * Sets the tap key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in tap mode.
     *
     * @param tapKey the key to be set as the tap key for the auto-clicker
     */
    public void setTapKey(String tapKey) {
        this.tapKey = tapKey;
    }

    /**
     * Retrieves the secondary start key for the auto-clicker. This key is used to
     * start the auto-clicking process when the auto-clicker is in toggle mode and
     * double-key activation is required. If double-key activation is not required
     * or the auto-clicker is in hold or tap mode, this key is ignored.
     *
     * @return the secondary start key as a String
     */
    public String getSecondaryStartKey() {
        return secondaryStartKey;
    }

    /**
     * Sets the secondary start key for the auto-clicker. This key is used to
     * start the auto-clicking process when the auto-clicker is in toggle mode and
     * double-key activation is required. If double-key activation is not required
     * or the auto-clicker is in hold or tap mode, this key is ignored.
     *
     * @param secondaryStartKey the key to be set as the secondary start key for
     *                          the auto-clicker
     */
    public void setSecondaryStartKey(String secondaryStartKey) {
        this.secondaryStartKey = secondaryStartKey;
    }

    /**
     * Retrieves the secondary hold key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in hold mode and
     * double-key activation is required. If double-key activation is not required
     * or the auto-clicker is in toggle or tap mode, this key is ignored.
     *
     * @return the secondary hold key as a String
     */
    public String getSecondaryHoldKey() {
        return secondaryHoldKey;
    }

    /**
     * Sets the secondary hold key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in hold mode and
     * double-key activation is required. If double-key activation is not required
     * or the auto-clicker is in toggle or tap mode, this key is ignored.
     *
     * @param secondaryHoldKey the key to be set as the secondary hold key for
     *                         the auto-clicker
     */
    public void setSecondaryHoldKey(String secondaryHoldKey) {
        this.secondaryHoldKey = secondaryHoldKey;
    }

    /**
     * Retrieves the secondary tap key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in tap mode and
     * double-key activation is required. If double-key activation is not required
     * or the auto-clicker is in toggle or hold mode, this key is ignored.
     *
     * @return the secondary tap key as a String
     */
    public String getSecondaryTapKey() {
        return secondaryTapKey;
    }

    /**
     * Sets the secondary tap key for the auto-clicker. This key is used to
     * activate the auto-clicking process when the auto-clicker is in tap mode and
     * double-key activation is required. If double-key activation is not required
     * or the auto-clicker is in toggle or hold mode, this key is ignored.
     *
     * @param secondaryTapKey the key to be set as the secondary tap key for
     *                        the auto-clicker
     */
    public void setSecondaryTapKey(String secondaryTapKey) {
        this.secondaryTapKey = secondaryTapKey;
    }
}
