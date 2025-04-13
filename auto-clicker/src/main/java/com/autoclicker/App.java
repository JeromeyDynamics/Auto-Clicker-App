package com.autoclicker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.JFormattedTextField;

public class App {
    private static JFrame settingsFrame;
    private static JComboBox<String> modeComboBox;
    private static JCheckBox disableCheckbox; // Global disable auto-clicking
    private static JCheckBox doubleKeyCheckbox; // Global: require double key press

    // Toggle mode fields.
    private static JTextField toggleStartField;
    private static JTextField toggleStopField;
    private static JTextField toggleSecondaryField; // Secondary key for toggle mode
    private static JSpinner toggleDurationSpinner;
    private static JCheckBox toggleUnlimitedCheckbox;
    private static JSpinner toggleSpeedSpinner;

    // Hold mode fields.
    private static JTextField holdKeyField;
    private static JTextField holdSecondaryField; // Secondary key for hold mode
    private static JSpinner holdSpeedSpinner;

    // Tap mode fields.
    private static JTextField tapKeyField;
    private static JTextField tapSecondaryField; // Secondary key for tap mode
    private static JSpinner tapDurationSpinner;
    private static JSpinner tapSpeedSpinner;

    private UserSettings settings;
    private SettingsManager settingsManager;

    public App() {
        settingsManager = new SettingsManager();
        settings = settingsManager.loadSettings();
    }

    public void showSettingsWindow() {
        if (settingsFrame == null) {
            settingsFrame = new JFrame("Auto Clicker Settings");
            settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            settingsFrame.setSize(500, 550);
            settingsFrame.setLocationRelativeTo(null);
            settingsFrame.setLayout(new GridBagLayout());
            settingsFrame.getContentPane().setBackground(new Color(45, 45, 45));

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Global disable checkbox.
            gbc.gridx = 0;
            gbc.gridy = 0;
            disableCheckbox = new JCheckBox("Disable Auto Clicking");
            disableCheckbox.setFont(fieldFont);
            disableCheckbox.setBackground(new Color(45, 45, 45));
            disableCheckbox.setForeground(Color.WHITE);
            disableCheckbox.setSelected(settings.isAutoClickerDisabled());
            settingsFrame.add(disableCheckbox, gbc);
            disableCheckbox.addActionListener(e -> autoSaveSettings());

            // Global double-key checkbox.
            gbc.gridx = 1;
            doubleKeyCheckbox = new JCheckBox("Require Double Key Activation");
            doubleKeyCheckbox.setFont(fieldFont);
            doubleKeyCheckbox.setBackground(new Color(45, 45, 45));
            doubleKeyCheckbox.setForeground(Color.WHITE);
            doubleKeyCheckbox.setSelected(settings.isRequireDoubleKey());
            settingsFrame.add(doubleKeyCheckbox, gbc);
            doubleKeyCheckbox.addActionListener(e -> updateDoubleKeyFieldsEnabled());

            // Global mode selection.
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
            // Toggle Panel.
            JPanel togglePanel = new JPanel(new GridBagLayout());
            togglePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Toggle Mode Settings",
                    0, 0, labelFont, Color.WHITE));
            togglePanel.setBackground(new Color(60, 60, 60));
            GridBagConstraints tgbc = new GridBagConstraints();
            tgbc.insets = new Insets(4, 4, 4, 4);
            tgbc.anchor = GridBagConstraints.WEST;

            // Row 0: Primary Start and Stop keys.
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

            // Row 1: Secondary key.
            tgbc.gridx = 0;
            tgbc.gridy = 1;
            JLabel toggleSecondaryLabel = new JLabel("Secondary Start Key:");
            toggleSecondaryLabel.setForeground(Color.WHITE);
            toggleSecondaryLabel.setFont(labelFont);
            togglePanel.add(toggleSecondaryLabel, tgbc);

            tgbc.gridx = 1;
            toggleSecondaryField = createKeyCaptureField(settings.getSecondaryStartKey());
            togglePanel.add(toggleSecondaryField, tgbc);

            // Row 2: Duration spinner and Unlimited checkbox.
            tgbc.gridx = 0;
            tgbc.gridy = 2;
            JLabel durationLabel = new JLabel("Time Limit (sec):");
            durationLabel.setForeground(Color.WHITE);
            durationLabel.setFont(labelFont);
            togglePanel.add(durationLabel, tgbc);

            tgbc.gridx = 1;
            toggleDurationSpinner = new JSpinner(new SpinnerNumberModel(settings.getToggleDuration(), 1, 60, 1));
            toggleDurationSpinner.setFont(fieldFont);
            togglePanel.add(toggleDurationSpinner, tgbc);
            toggleDurationSpinner.addChangeListener(e -> autoSaveSettings());
            addClampFocusListener(toggleDurationSpinner, 1, 60);

            tgbc.gridx = 2;
            toggleUnlimitedCheckbox = new JCheckBox("Unlimited");
            toggleUnlimitedCheckbox.setFont(fieldFont);
            toggleUnlimitedCheckbox.setBackground(new Color(60, 60, 60));
            toggleUnlimitedCheckbox.setForeground(Color.WHITE);
            toggleUnlimitedCheckbox.setSelected(settings.isToggleUnlimited());
            togglePanel.add(toggleUnlimitedCheckbox, tgbc);
            toggleUnlimitedCheckbox.addActionListener(e -> autoSaveSettings());

            // Row 3: Speed spinner.
            tgbc.gridx = 0;
            tgbc.gridy = 3;
            JLabel toggleSpeedLabel = new JLabel("Speed (1-10):");
            toggleSpeedLabel.setForeground(Color.WHITE);
            toggleSpeedLabel.setFont(labelFont);
            togglePanel.add(toggleSpeedLabel, tgbc);

            tgbc.gridx = 1;
            toggleSpeedSpinner = new JSpinner(new SpinnerNumberModel(settings.getToggleSpeed(), 1, 10, 1));
            toggleSpeedSpinner.setFont(fieldFont);
            togglePanel.add(toggleSpeedSpinner, tgbc);
            toggleSpeedSpinner.addChangeListener(e -> autoSaveSettings());
            addClampFocusListener(toggleSpeedSpinner, 1, 10);

            // Hold Panel.
            JPanel holdPanel = new JPanel(new GridBagLayout());
            holdPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Hold Mode Settings",
                    0, 0, labelFont, Color.WHITE));
            holdPanel.setBackground(new Color(60, 60, 60));
            GridBagConstraints hgbc = new GridBagConstraints();
            hgbc.insets = new Insets(4, 4, 4, 4);
            hgbc.anchor = GridBagConstraints.WEST;

            hgbc.gridx = 0;
            hgbc.gridy = 0;
            JLabel holdKeyLabel = new JLabel("Hold Key:");
            holdKeyLabel.setForeground(Color.WHITE);
            holdKeyLabel.setFont(labelFont);
            holdPanel.add(holdKeyLabel, hgbc);

            hgbc.gridx = 1;
            holdKeyField = createKeyCaptureField(settings.getHoldKey());
            holdPanel.add(holdKeyField, hgbc);

            hgbc.gridx = 0;
            hgbc.gridy = 1;
            JLabel holdSecondaryLabel = new JLabel("Secondary Hold Key:");
            holdSecondaryLabel.setForeground(Color.WHITE);
            holdSecondaryLabel.setFont(labelFont);
            holdPanel.add(holdSecondaryLabel, hgbc);

            hgbc.gridx = 1;
            holdSecondaryField = createKeyCaptureField(settings.getSecondaryHoldKey());
            holdPanel.add(holdSecondaryField, hgbc);

            hgbc.gridx = 0;
            hgbc.gridy = 2;
            JLabel holdSpeedLabel = new JLabel("Speed (1-10):");
            holdSpeedLabel.setForeground(Color.WHITE);
            holdSpeedLabel.setFont(labelFont);
            holdPanel.add(holdSpeedLabel, hgbc);

            hgbc.gridx = 1;
            holdSpeedSpinner = new JSpinner(new SpinnerNumberModel(settings.getHoldSpeed(), 1, 10, 1));
            holdSpeedSpinner.setFont(fieldFont);
            holdPanel.add(holdSpeedSpinner, hgbc);
            holdSpeedSpinner.addChangeListener(e -> autoSaveSettings());
            addClampFocusListener(holdSpeedSpinner, 1, 10);

            // Tap Panel.
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
            JLabel tapSecondaryLabel = new JLabel("Secondary Tap Key:");
            tapSecondaryLabel.setForeground(Color.WHITE);
            tapSecondaryLabel.setFont(labelFont);
            tapPanel.add(tapSecondaryLabel, tpgbc);

            tpgbc.gridx = 1;
            tapSecondaryField = createKeyCaptureField(settings.getSecondaryTapKey());
            tapPanel.add(tapSecondaryField, tpgbc);

            tpgbc.gridx = 0;
            tpgbc.gridy = 2;
            JLabel tapDurationLabel = new JLabel("Burst Duration (sec):");
            tapDurationLabel.setForeground(Color.WHITE);
            tapDurationLabel.setFont(labelFont);
            tapPanel.add(tapDurationLabel, tpgbc);

            tpgbc.gridx = 1;
            tapDurationSpinner = new JSpinner(new SpinnerNumberModel(settings.getTapDuration(), 1, 60, 1));
            tapDurationSpinner.setFont(fieldFont);
            tapPanel.add(tapDurationSpinner, tpgbc);
            tapDurationSpinner.addChangeListener(e -> autoSaveSettings());
            addClampFocusListener(tapDurationSpinner, 1, 60);

            tpgbc.gridx = 0;
            tpgbc.gridy = 3;
            JLabel tapSpeedLabel = new JLabel("Speed (1-10):");
            tapSpeedLabel.setForeground(Color.WHITE);
            tapSpeedLabel.setFont(labelFont);
            tapPanel.add(tapSpeedLabel, tpgbc);

            tpgbc.gridx = 1;
            tapSpeedSpinner = new JSpinner(new SpinnerNumberModel(settings.getTapSpeed(), 1, 10, 1));
            tapSpeedSpinner.setFont(fieldFont);
            tapPanel.add(tapSpeedSpinner, tpgbc);
            tapSpeedSpinner.addChangeListener(e -> autoSaveSettings());
            addClampFocusListener(tapSpeedSpinner, 1, 10);

            // CardLayout panel for mode panels.
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            JPanel modePanel = new JPanel(new CardLayout());
            modePanel.setBackground(new Color(45, 45, 45));
            modePanel.add(togglePanel, "Toggle");
            modePanel.add(holdPanel, "Hold");
            modePanel.add(tapPanel, "Tap");
            settingsFrame.add(modePanel, gbc);

            modeComboBox.addActionListener(e -> {
                CardLayout cl = (CardLayout) modePanel.getLayout();
                String selected = (String) modeComboBox.getSelectedItem();
                cl.show(modePanel, selected);
                autoSaveSettings();
            });

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
            // Ensure double key fields are enabled/disabled based on checkbox.
            updateDoubleKeyFieldsEnabled();
        } else {
            settingsFrame.setVisible(true);
        }
    }

    // Save settings from UI.
    private void autoSaveSettings() {
        String mode = ((String) modeComboBox.getSelectedItem()).toLowerCase();
        settings.setMode(mode);
        settings.setAutoClickerDisabled(disableCheckbox.isSelected());
        settings.setRequireDoubleKey(doubleKeyCheckbox.isSelected());

        if (mode.equals("toggle")) {
            settings.setStartKey(toggleStartField.getText());
            settings.setStopKey(toggleStopField.getText());
            settings.setSecondaryStartKey(doubleKeyCheckbox.isSelected() ? toggleSecondaryField.getText() : "");
            settings.setToggleDuration((Integer) toggleDurationSpinner.getValue());
            settings.setToggleUnlimited(toggleUnlimitedCheckbox.isSelected());
            settings.setToggleSpeed((Integer) toggleSpeedSpinner.getValue());
        } else if (mode.equals("hold")) {
            settings.setHoldKey(holdKeyField.getText());
            settings.setSecondaryHoldKey(doubleKeyCheckbox.isSelected() ? holdSecondaryField.getText() : "");
            settings.setHoldSpeed((Integer) holdSpeedSpinner.getValue());
        } else if (mode.equals("tap")) {
            settings.setTapKey(tapKeyField.getText());
            settings.setSecondaryTapKey(doubleKeyCheckbox.isSelected() ? tapSecondaryField.getText() : "");
            settings.setTapDuration((Integer) tapDurationSpinner.getValue());
            settings.setTapSpeed((Integer) tapSpeedSpinner.getValue());
        }
        settingsManager.saveSettings(settings);
    }

    // Enable or disable the secondary key fields based on the doubleKeyCheckbox.
    private void updateDoubleKeyFieldsEnabled() {
        boolean enabled = doubleKeyCheckbox.isSelected();
        if (toggleSecondaryField != null)
            toggleSecondaryField.setEnabled(enabled);
        if (holdSecondaryField != null)
            holdSecondaryField.setEnabled(enabled);
        if (tapSecondaryField != null)
            tapSecondaryField.setEnabled(enabled);
        autoSaveSettings();
    }

    // Helper method for key capture.
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

    // Helper to clamp spinner input when focus is lost.
    private void addClampFocusListener(JSpinner spinner, Comparable min, Comparable max) {
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        ftf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    spinner.commitEdit();
                } catch (java.text.ParseException ex) {
                }
                Number value = (Number) spinner.getValue();
                if (value.doubleValue() < ((Number) min).doubleValue()) {
                    spinner.setValue(min);
                } else if (value.doubleValue() > ((Number) max).doubleValue()) {
                    spinner.setValue(max);
                }
            }
        });
    }

    // Helper to capitalize strings.
    private String capitalize(String s) {
        if (s == null || s.isEmpty())
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    // Provide access to the settings window.
    public static JFrame getSettingsFrame() {
        return settingsFrame;
    }
}
