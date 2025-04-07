package com.autoclicker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class App {
    private static JFrame settingsFrame;
    private static JSpinner durationSpinner;
    private static JComboBox<String> modeComboBox;
    private static JTextField toggleStartField;
    private static JTextField toggleStopField;
    private static JTextField holdKeyField;

    private UserSettings settings;
    private SettingsManager settingsManager;

    public App() {
        settings = new UserSettings();
        settingsManager = new SettingsManager();
    }

    public void showSettingsWindow() {
        if (settingsFrame == null) {
            settingsFrame = new JFrame("Auto Clicker Settings");
            settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            settingsFrame.setSize(400, 300);
            settingsFrame.setLocationRelativeTo(null);
            settingsFrame.setLayout(new GridBagLayout());
            // Set a dark background color
            settingsFrame.getContentPane().setBackground(new Color(45, 45, 45));

            // Define custom fonts
            Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Click duration spinner label
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel durationLabel = new JLabel("Click duration (seconds):");
            durationLabel.setForeground(Color.WHITE);
            durationLabel.setFont(labelFont);
            settingsFrame.add(durationLabel, gbc);

            // Click duration spinner
            durationSpinner = new JSpinner(new SpinnerNumberModel(settings.getClickDuration(), 1, 60, 1));
            durationSpinner.setFont(fieldFont);
            gbc.gridx = 1;
            settingsFrame.add(durationSpinner, gbc);

            // Mode selection label
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel modeLabel = new JLabel("Mode:");
            modeLabel.setForeground(Color.WHITE);
            modeLabel.setFont(labelFont);
            settingsFrame.add(modeLabel, gbc);

            // Mode selection combo box
            modeComboBox = new JComboBox<>(new String[] { "Toggle", "Hold" });
            modeComboBox.setFont(fieldFont);
            modeComboBox.setSelectedItem(settings.getMode().equals("hold") ? "Hold" : "Toggle");
            gbc.gridx = 1;
            settingsFrame.add(modeComboBox, gbc);

            // Panel for Toggle Mode keys
            JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            togglePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Toggle Mode Keys",
                    0, 0, labelFont, Color.WHITE));
            togglePanel.setBackground(new Color(60, 60, 60));
            toggleStartField = createKeyCaptureField(settings.getStartKey());
            toggleStopField = createKeyCaptureField(settings.getStopKey());
            JLabel startKeyLabel = new JLabel("Start Key:");
            startKeyLabel.setForeground(Color.WHITE);
            startKeyLabel.setFont(labelFont);
            JLabel stopKeyLabel = new JLabel("Stop Key:");
            stopKeyLabel.setForeground(Color.WHITE);
            stopKeyLabel.setFont(labelFont);
            togglePanel.add(startKeyLabel);
            togglePanel.add(toggleStartField);
            togglePanel.add(stopKeyLabel);
            togglePanel.add(toggleStopField);

            // Panel for Hold Mode key
            JPanel holdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            holdPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    "Hold Mode Key",
                    0, 0, labelFont, Color.WHITE));
            holdPanel.setBackground(new Color(60, 60, 60));
            holdKeyField = createKeyCaptureField(settings.getHoldKey());
            JLabel holdKeyLabel = new JLabel("Hold Key:");
            holdKeyLabel.setForeground(Color.WHITE);
            holdKeyLabel.setFont(labelFont);
            holdPanel.add(holdKeyLabel);
            holdPanel.add(holdKeyField);

            // CardLayout panel to switch between togglePanel and holdPanel
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            JPanel modePanel = new JPanel(new CardLayout());
            modePanel.setBackground(new Color(45, 45, 45));
            modePanel.add(togglePanel, "Toggle");
            modePanel.add(holdPanel, "Hold");
            settingsFrame.add(modePanel, gbc);

            // Change visible panel based on mode selection
            modeComboBox.addActionListener(e -> {
                CardLayout cl = (CardLayout) (modePanel.getLayout());
                String selected = (String) modeComboBox.getSelectedItem();
                cl.show(modePanel, selected);
            });
            // Set initial panel
            CardLayout cl = (CardLayout) (modePanel.getLayout());
            cl.show(modePanel, (String) modeComboBox.getSelectedItem());

            // Save button to store settings
            JButton saveButton = new JButton("Save");
            saveButton.setFont(fieldFont);
            saveButton.setBackground(new Color(80, 80, 80));
            saveButton.setForeground(Color.WHITE);
            saveButton.setFocusPainted(false);
            // Add simple hover effect
            saveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    saveButton.setBackground(new Color(100, 100, 100));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    saveButton.setBackground(new Color(80, 80, 80));
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            settingsFrame.add(saveButton, gbc);
            saveButton.addActionListener(e -> {
                // Update settings from UI values
                settings.setClickDuration((int) durationSpinner.getValue());
                String mode = ((String) modeComboBox.getSelectedItem()).toLowerCase();
                settings.setMode(mode);
                if (mode.equals("toggle")) {
                    settings.setStartKey(toggleStartField.getText());
                    settings.setStopKey(toggleStopField.getText());
                } else {
                    settings.setHoldKey(holdKeyField.getText());
                }
                // Save to disk
                settingsManager.saveSettings(settings);
                JOptionPane.showMessageDialog(settingsFrame, "Settings saved.");
            });
        }

        // Apply fade-in animation only if the frame is undecorated.
        // If decorated, simply show the frame.
        if (!settingsFrame.isUndecorated()) {
            // Fallback: skip fade-in animation for decorated frames.
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
    }

    // Helper method to create a styled key capture field
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
            }
        });
        return field;
    }
}
