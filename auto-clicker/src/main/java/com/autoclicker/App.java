package com.autoclicker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class App {
    private static JFrame settingsFrame;
    private static JComboBox<String> modeComboBox;
    private static JCheckBox disableCheckbox; // Global disable checkbox

    // Fields for Toggle mode.
    private static JTextField toggleStartField;
    private static JTextField toggleStopField;
    private static JSpinner toggleDurationSpinner;
    private static JCheckBox toggleUnlimitedCheckbox;

    // Field for Hold mode.
    private static JTextField holdKeyField;

    // Fields for Tap mode.
    private static JTextField tapKeyField;
    private static JSpinner tapDurationSpinner; // Burst duration spinner

    private UserSettings settings;
    private SettingsManager settingsManager;

    public App() {
        settingsManager = new SettingsManager();
        // Load current settings from disk.
        settings = settingsManager.loadSettings();
    }

    public void showSettingsWindow() {
        if (settingsFrame == null) {
            settingsFrame = new JFrame("Auto Clicker Settings");
            settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            settingsFrame.setSize(400, 450);
            settingsFrame.setLocationRelativeTo(null);
            settingsFrame.setLayout(new GridBagLayout());
            // Set dark background color.
            settingsFrame.getContentPane().setBackground(new Color(45, 45, 45));

            // Define custom fonts.
            Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Global Disable Checkbox (at the top).
            gbc.gridx = 0;
            gbc.gridy = 0;
            disableCheckbox = new JCheckBox("Disable Auto Clicking");
            disableCheckbox.setFont(fieldFont);
            disableCheckbox.setBackground(new Color(45, 45, 45));
            disableCheckbox.setForeground(Color.WHITE);
            disableCheckbox.setSelected(settings.isAutoClickerDisabled());
            settingsFrame.add(disableCheckbox, gbc);
            disableCheckbox.addActionListener(e -> autoSaveSettings());

            // Global Mode selection.
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel modeLabel = new JLabel("Mode:");
            modeLabel.setForeground(Color.WHITE);
            modeLabel.setFont(labelFont);
            settingsFrame.add(modeLabel, gbc);

            modeComboBox = new JComboBox<>(new String[] { "Toggle", "Hold", "Tap" });
            modeComboBox.setFont(fieldFont);
            modeComboBox.setSelectedItem(capitalize(settings.getMode()));
            gbc.gridx = 1;
            settingsFrame.add(modeComboBox, gbc);

            // Create panels for each mode.
            // Toggle Panel: Contains key fields plus a duration spinner and an "Unlimited"
            // checkbox.
            JPanel togglePanel = new JPanel(new GridBagLayout());
            togglePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Toggle Mode Settings",
                    0, 0, labelFont, Color.WHITE));
            togglePanel.setBackground(new Color(60, 60, 60));
            GridBagConstraints tgbc = new GridBagConstraints();
            tgbc.insets = new Insets(4, 4, 4, 4);
            tgbc.anchor = GridBagConstraints.WEST;

            // Row 0: Start and Stop keys.
            tgbc.gridx = 0;
            tgbc.gridy = 0;
            JLabel startKeyLabel = new JLabel("Start Key:");
            startKeyLabel.setForeground(Color.WHITE);
            startKeyLabel.setFont(labelFont);
            togglePanel.add(startKeyLabel, tgbc);

            tgbc.gridx = 1;
            toggleStartField = createKeyCaptureField(settings.getStartKey());
            togglePanel.add(toggleStartField, tgbc);

            tgbc.gridx = 2;
            JLabel stopKeyLabel = new JLabel("Stop Key:");
            stopKeyLabel.setForeground(Color.WHITE);
            stopKeyLabel.setFont(labelFont);
            togglePanel.add(stopKeyLabel, tgbc);

            tgbc.gridx = 3;
            toggleStopField = createKeyCaptureField(settings.getStopKey());
            togglePanel.add(toggleStopField, tgbc);

            // Row 1: Duration spinner and Unlimited checkbox.
            tgbc.gridx = 0;
            tgbc.gridy = 1;
            JLabel durationLabel = new JLabel("Time Limit (sec):");
            durationLabel.setForeground(Color.WHITE);
            durationLabel.setFont(labelFont);
            togglePanel.add(durationLabel, tgbc);

            tgbc.gridx = 1;
            toggleDurationSpinner = new JSpinner(new SpinnerNumberModel(settings.getToggleDuration(), 1, 60, 1));
            toggleDurationSpinner.setFont(fieldFont);
            togglePanel.add(toggleDurationSpinner, tgbc);
            toggleDurationSpinner.addChangeListener(e -> autoSaveSettings());

            tgbc.gridx = 2;
            toggleUnlimitedCheckbox = new JCheckBox("Unlimited");
            toggleUnlimitedCheckbox.setFont(fieldFont);
            toggleUnlimitedCheckbox.setBackground(new Color(60, 60, 60));
            toggleUnlimitedCheckbox.setForeground(Color.WHITE);
            toggleUnlimitedCheckbox.setSelected(settings.isToggleUnlimited());
            togglePanel.add(toggleUnlimitedCheckbox, tgbc);
            toggleUnlimitedCheckbox.addActionListener(e -> autoSaveSettings());

            // Hold Panel: Contains only one key field.
            JPanel holdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            holdPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Hold Mode Settings",
                    0, 0, labelFont, Color.WHITE));
            holdPanel.setBackground(new Color(60, 60, 60));
            holdKeyField = createKeyCaptureField(settings.getHoldKey());
            JLabel holdKeyLabel = new JLabel("Hold Key:");
            holdKeyLabel.setForeground(Color.WHITE);
            holdKeyLabel.setFont(labelFont);
            holdPanel.add(holdKeyLabel);
            holdPanel.add(holdKeyField);

            // Tap Panel: Contains a key field and a burst duration spinner.
            JPanel tapPanel = new JPanel(new GridBagLayout());
            tapPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Tap Mode Settings",
                    0, 0, labelFont, Color.WHITE));
            tapPanel.setBackground(new Color(60, 60, 60));
            GridBagConstraints tpgbc = new GridBagConstraints();
            tpgbc.insets = new Insets(4, 4, 4, 4);
            tpgbc.anchor = GridBagConstraints.WEST;

            tpgbc.gridx = 0;
            tpgbc.gridy = 0;
            JLabel tapKeyLabel = new JLabel("Tap Key:");
            tapKeyLabel.setForeground(Color.WHITE);
            tapKeyLabel.setFont(labelFont);
            tapPanel.add(tapKeyLabel, tpgbc);

            tpgbc.gridx = 1;
            tapKeyField = createKeyCaptureField(settings.getTapKey());
            tapPanel.add(tapKeyField, tpgbc);

            tpgbc.gridx = 0;
            tpgbc.gridy = 1;
            JLabel tapDurationLabel = new JLabel("Burst Duration (sec):");
            tapDurationLabel.setForeground(Color.WHITE);
            tapDurationLabel.setFont(labelFont);
            tapPanel.add(tapDurationLabel, tpgbc);

            tpgbc.gridx = 1;
            tapDurationSpinner = new JSpinner(new SpinnerNumberModel(settings.getTapDuration(), 1, 60, 1));
            tapDurationSpinner.setFont(fieldFont);
            tapPanel.add(tapDurationSpinner, tpgbc);
            tapDurationSpinner.addChangeListener(e -> autoSaveSettings());

            // CardLayout panel to switch between mode panels.
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            final JPanel modePanel = new JPanel(new CardLayout());
            modePanel.setBackground(new Color(45, 45, 45));
            modePanel.add(togglePanel, "Toggle");
            modePanel.add(holdPanel, "Hold");
            modePanel.add(tapPanel, "Tap");
            settingsFrame.add(modePanel, gbc);

            // When mode selection changes, update the visible panel and auto-save.
            modeComboBox.addActionListener(e -> {
                CardLayout cl = (CardLayout) (modePanel.getLayout());
                String selected = (String) modeComboBox.getSelectedItem();
                cl.show(modePanel, selected);
                autoSaveSettings();
            });

            // Show the settings window.
            if (!settingsFrame.isUndecorated()) {
                settingsFrame.setOpacity(1f);
                settingsFrame.setVisible(true);
            } else {
                settingsFrame.setOpacity(0f);
                settingsFrame.setVisible(true);
                Timer timer = new Timer(20, new ActionListener() {
                    float opacity = 0f;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        opacity += 0.05f;
                        if (opacity >= 1f) {
                            opacity = 1f;
                            ((Timer) e.getSource()).stop();
                        }
                        settingsFrame.setOpacity(opacity);
                    }
                });
                timer.start();
            }
        } else {
            settingsFrame.setVisible(true);
        }
    }

    // Update settings from the UI and auto-save them.
    private void autoSaveSettings() {
        String mode = ((String) modeComboBox.getSelectedItem()).toLowerCase();
        settings.setMode(mode);
        // Global disable setting.
        settings.setAutoClickerDisabled(disableCheckbox.isSelected());

        if (mode.equals("toggle")) {
            settings.setStartKey(toggleStartField.getText());
            settings.setStopKey(toggleStopField.getText());
            settings.setToggleDuration((Integer) toggleDurationSpinner.getValue());
            settings.setToggleUnlimited(toggleUnlimitedCheckbox.isSelected());
        } else if (mode.equals("tap")) {
            settings.setTapKey(tapKeyField.getText());
            settings.setTapDuration((Integer) tapDurationSpinner.getValue());
        } else { // hold mode.
            settings.setHoldKey(holdKeyField.getText());
        }
        settingsManager.saveSettings(settings);
    }

    // Helper method to create a styled key capture field with auto-save on key
    // press.
    private JTextField createKeyCaptureField(String initialValue) {
        JTextField field = new JTextField(initialValue, 5);
        field.setEditable(false);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(80, 80, 80));
        field.setForeground(Color.WHITE);
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.setText("Press key...");
                field.requestFocusInWindow();
            }
        });
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                field.setText(keyText);
                autoSaveSettings();
            }
        });
        return field;
    }

    // Helper to capitalize the first letter (for proper combo box selection).
    private String capitalize(String s) {
        if (s == null || s.isEmpty())
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
