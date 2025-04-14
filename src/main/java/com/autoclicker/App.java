package com.autoclicker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

public class App {
    private static JFrame settingsFrame;
    private static JComboBox<String> modeComboBox;
    private static JCheckBox disableCheckbox;
    private static JCheckBox doubleKeyCheckbox;

    private static JTextField toggleStartField, toggleStopField, toggleSecondaryField;
    private static JSpinner toggleDurationSpinner, toggleSpeedSpinner;
    private static JCheckBox toggleUnlimitedCheckbox;

    private static JTextField holdKeyField, holdSecondaryField;
    private static JSpinner holdSpeedSpinner;

    private static JTextField tapKeyField, tapSecondaryField;
    private static JSpinner tapDurationSpinner, tapSpeedSpinner;

    private UserSettings settings;
    private SettingsManager settingsManager;

    public App() {
        settingsManager = new SettingsManager();
        settings = settingsManager.loadSettings();
    }

    /**
     * Shows the settings window to the user. If the window is already created, it
     * will be shown.
     * Otherwise, it will be created and shown. The window is modal, so it will not
     * allow the user
     * to interact with the main window until it is closed.
     */
    public void showSettingsWindow() {
        if (settingsFrame == null) {
            settingsFrame = new JFrame();
            settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            settingsFrame.setSize(580, 620);
            settingsFrame.setLocationRelativeTo(null);
            settingsFrame.setUndecorated(false);
            settingsFrame.getContentPane().setBackground(new Color(40, 40, 40));
            settingsFrame.setLayout(new BorderLayout());

            // Title bar with icon and app name
            JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titleBar.setBackground(new Color(30, 30, 30));
            titleBar.setBorder(new EmptyBorder(10, 10, 10, 10));

            Image icon = Toolkit.getDefaultToolkit().getImage("auto-clicker\\src\\main\\res\\img\\pixil-frame-0.png");
            JLabel iconLabel = new JLabel(new ImageIcon(icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
            JLabel titleLabel = new JLabel("Auto Clicker Open Source");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);

            titleBar.add(iconLabel);
            titleBar.add(Box.createRigidArea(new Dimension(10, 0)));
            titleBar.add(titleLabel);
            settingsFrame.add(titleBar, BorderLayout.NORTH);

            JPanel content = new JPanel(new GridBagLayout());
            content.setBackground(new Color(45, 45, 45));
            content.setBorder(new EmptyBorder(20, 20, 20, 20));

            Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Row 0: Global checkboxes
            disableCheckbox = new JCheckBox("Disable Auto Clicking");
            doubleKeyCheckbox = new JCheckBox("Require Double Key Activation");

            styleCheckBox(disableCheckbox, fieldFont);
            styleCheckBox(doubleKeyCheckbox, fieldFont);

            disableCheckbox.setSelected(settings.isAutoClickerDisabled());
            doubleKeyCheckbox.setSelected(settings.isRequireDoubleKey());

            disableCheckbox.addActionListener(e -> autoSaveSettings());
            doubleKeyCheckbox.addActionListener(e -> updateDoubleKeyFieldsEnabled());

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            content.add(disableCheckbox, gbc);

            gbc.gridx = 1;
            content.add(doubleKeyCheckbox, gbc);

            // Row 1: Mode dropdown
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel modeLabel = new JLabel("Mode:");
            modeLabel.setForeground(Color.WHITE);
            modeLabel.setFont(labelFont);
            content.add(modeLabel, gbc);

            modeComboBox = new JComboBox<>(new String[] { "Toggle", "Hold", "Tap" });
            modeComboBox.setFont(fieldFont);
            modeComboBox.setSelectedItem(capitalize(settings.getMode()));
            gbc.gridx = 1;
            content.add(modeComboBox, gbc);

            // Mode-specific panels
            JPanel togglePanel = buildTogglePanel(labelFont, fieldFont);
            JPanel holdPanel = buildHoldPanel(labelFont, fieldFont);
            JPanel tapPanel = buildTapPanel(labelFont, fieldFont);

            JPanel modePanel = new JPanel(new CardLayout());
            modePanel.setBackground(new Color(45, 45, 45));
            modePanel.add(togglePanel, "Toggle");
            modePanel.add(holdPanel, "Hold");
            modePanel.add(tapPanel, "Tap");

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;
            content.add(modePanel, gbc);

            modeComboBox.addActionListener(e -> {
                CardLayout cl = (CardLayout) modePanel.getLayout();
                cl.show(modePanel, (String) modeComboBox.getSelectedItem());
                autoSaveSettings();
            });

            settingsFrame.add(content, BorderLayout.CENTER);
            settingsFrame.setVisible(true);

            updateDoubleKeyFieldsEnabled();
        } else {
            settingsFrame.setVisible(true);
        }
    }

    /**
     * Builds the panel for toggle mode settings.
     *
     * @param labelFont font for labels
     * @param fieldFont font for fields
     * @return the panel
     */
    private JPanel buildTogglePanel(Font labelFont, Font fieldFont) {
        JPanel panel = createModePanel("Toggle Mode Settings", labelFont);
        GridBagConstraints gbc = defaultConstraints();

        panel.add(makeLabeledField("Start Key:", toggleStartField = createKeyCaptureField(settings.getStartKey()),
                labelFont), gbc);
        gbc.gridy++;
        panel.add(makeLabeledField("Stop Key:", toggleStopField = createKeyCaptureField(settings.getStopKey()),
                labelFont), gbc);
        gbc.gridy++;
        panel.add(makeLabeledField("Secondary Start Key:",
                toggleSecondaryField = createKeyCaptureField(settings.getSecondaryStartKey()), labelFont), gbc);
        gbc.gridy++;

        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toggleDurationSpinner = new JSpinner(new SpinnerNumberModel(settings.getToggleDuration(), 1, 60, 1));
        styleSpinner(toggleDurationSpinner, fieldFont, 1, 60);
        toggleUnlimitedCheckbox = new JCheckBox("Unlimited");
        styleCheckBox(toggleUnlimitedCheckbox, fieldFont);
        toggleUnlimitedCheckbox.setSelected(settings.isToggleUnlimited());

        durationPanel.setBackground(panel.getBackground());
        durationPanel.add(toggleDurationSpinner);
        durationPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        durationPanel.add(toggleUnlimitedCheckbox);

        panel.add(makeLabeledField("Time Limit (sec):", durationPanel, labelFont), gbc);
        gbc.gridy++;

        toggleSpeedSpinner = new JSpinner(new SpinnerNumberModel(settings.getToggleSpeed(), 1, 10, 1));
        styleSpinner(toggleSpeedSpinner, fieldFont, 1, 10);
        panel.add(makeLabeledField("Speed (1-10):", toggleSpeedSpinner, labelFont), gbc);

        return panel;
    }

    /**
     * Builds the panel for hold mode settings.
     *
     * @param labelFont font for labels
     * @param fieldFont font for fields
     * @return the panel
     */
    private JPanel buildHoldPanel(Font labelFont, Font fieldFont) {
        JPanel panel = createModePanel("Hold Mode Settings", labelFont);
        GridBagConstraints gbc = defaultConstraints();

        panel.add(makeLabeledField("Hold Key:", holdKeyField = createKeyCaptureField(settings.getHoldKey()), labelFont),
                gbc);
        gbc.gridy++;
        panel.add(makeLabeledField("Secondary Hold Key:",
                holdSecondaryField = createKeyCaptureField(settings.getSecondaryHoldKey()), labelFont), gbc);
        gbc.gridy++;

        holdSpeedSpinner = new JSpinner(new SpinnerNumberModel(settings.getHoldSpeed(), 1, 10, 1));
        styleSpinner(holdSpeedSpinner, fieldFont, 1, 10);
        panel.add(makeLabeledField("Speed (1-10):", holdSpeedSpinner, labelFont), gbc);

        return panel;
    }

    /**
     * Builds the panel for tap mode settings.
     *
     * @param labelFont font for labels
     * @param fieldFont font for fields
     * @return the panel
     */
    private JPanel buildTapPanel(Font labelFont, Font fieldFont) {
        JPanel panel = createModePanel("Tap Mode Settings", labelFont);
        GridBagConstraints gbc = defaultConstraints();

        panel.add(makeLabeledField("Tap Key:", tapKeyField = createKeyCaptureField(settings.getTapKey()), labelFont),
                gbc);
        gbc.gridy++;
        panel.add(makeLabeledField("Secondary Tap Key:",
                tapSecondaryField = createKeyCaptureField(settings.getSecondaryTapKey()), labelFont), gbc);
        gbc.gridy++;

        tapDurationSpinner = new JSpinner(new SpinnerNumberModel(settings.getTapDuration(), 1, 60, 1));
        styleSpinner(tapDurationSpinner, fieldFont, 1, 60);
        panel.add(makeLabeledField("Burst Duration (sec):", tapDurationSpinner, labelFont), gbc);
        gbc.gridy++;

        tapSpeedSpinner = new JSpinner(new SpinnerNumberModel(settings.getTapSpeed(), 1, 10, 1));
        styleSpinner(tapSpeedSpinner, fieldFont, 1, 10);
        panel.add(makeLabeledField("Speed (1-10):", tapSpeedSpinner, labelFont), gbc);

        return panel;
    }

    /**
     * Creates a panel with a titled border for mode settings.
     * 
     * @param title     the title of the panel
     * @param titleFont the font for the title
     * @return the panel
     */
    private JPanel createModePanel(String title, Font titleFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(55, 55, 55));
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                title, TitledBorder.LEFT, TitledBorder.TOP, titleFont, Color.WHITE);
        panel.setBorder(border);
        return panel;
    }

    /**
     * Returns a GridBagConstraints object with default settings for this app.
     *
     * <ul>
     * <li>Insets of 6 pixels on all sides</li>
     * <li>Gridx and gridy set to 0</li>
     * <li>Fill set to HORIZONTAL</li>
     * <li>Weightx set to 1</li>
     * </ul>
     *
     * @return a GridBagConstraints object with default settings
     */
    private GridBagConstraints defaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    /**
     * Creates a panel with a label and a field component next to each other. The
     * panel has a background
     * color of a dark gray, and the label has the given font and a white foreground
     * color. The field is
     * added to the center of the panel, and the label is added to the west of the
     * panel.
     *
     * @param labelText the text for the label
     * @param field     the field component to add to the panel
     * @param font      the font for the label
     * @return a panel with the label and field
     */
    private JPanel makeLabeledField(String labelText, JComponent field, Font font) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(55, 55, 55));
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Sets the style of a JCheckBox to have a dark gray background, white
     * foreground, and the given font.
     * 
     * @param box  the JCheckBox to style
     * @param font the font to use for the JCheckBox
     */
    private void styleCheckBox(JCheckBox box, Font font) {
        box.setFont(font);
        box.setBackground(new Color(45, 45, 45));
        box.setForeground(Color.WHITE);
    }

    /**
     * Styles a JSpinner with the specified font and adds functionality to ensure
     * its value remains within a specified range. Also ensures that any changes
     * to the spinner's value automatically trigger a settings save.
     *
     * @param spinner the JSpinner to style
     * @param font    the font to set for the spinner
     * @param min     the minimum allowable value for the spinner
     * @param max     the maximum allowable value for the spinner
     */

    private void styleSpinner(JSpinner spinner, Font font, int min, int max) {
        spinner.setFont(font);
        addClampFocusListener(spinner, min, max);
        spinner.addChangeListener(e -> autoSaveSettings());
    }

    /**
     * Creates a non-editable text field for capturing key presses, initialized with
     * the given value.
     * The field displays "Press key..." when clicked, and updates to show the text
     * representation
     * of the pressed key. The field's appearance is styled with a dark background
     * and white text.
     *
     * @param initialValue the initial text to display in the field
     * @return a JTextField configured for key capture input
     */

    private JTextField createKeyCaptureField(String initialValue) {
        JTextField field = new JTextField(initialValue, 8);
        field.setEditable(false);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(70, 70, 70));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                field.setText("Press key...");
                field.requestFocusInWindow();
            }
        });
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                field.setText(KeyEvent.getKeyText(e.getKeyCode()));
                autoSaveSettings();
            }
        });
        return field;
    }

    /**
     * Saves the current state of the UI to the settings object, which is then
     * persisted to
     * file by the SettingsManager. This method is called whenever the user changes
     * any
     * setting in the UI.
     */
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

    /**
     * Updates the enabled state of the secondary key fields based on the state of
     * the double key
     * checkbox. If the checkbox is selected, the secondary key fields are enabled;
     * otherwise, they
     * are disabled. This method also causes the settings to be auto-saved by
     * calling
     * autoSaveSettings() after the fields have been updated.
     */
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

    /**
     * Adds a focus listener to the given spinner that ensures that the value does
     * not fall outside
     * of the given range. If the user enters a value and then tabs away or clicks
     * away, the value
     * will be clamped to the nearest boundary if it is outside of the range.
     *
     * @param spinner the spinner to add the listener to
     * @param min     the minimum value
     * @param max     the maximum value
     */
    private void addClampFocusListener(JSpinner spinner, Comparable min, Comparable max) {
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        ftf.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    spinner.commitEdit();
                } catch (Exception ignored) {
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

    /**
     * Returns a capitalized version of the given string, where the first character
     * is
     * uppercased and the rest of the string is lowercased. If the given string is
     * null or
     * empty, the original string is returned.
     *
     * @param s the string to capitalize
     * @return the capitalized string
     */
    private String capitalize(String s) {
        if (s == null || s.isEmpty())
            return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    /**
     * Returns the JFrame that contains the settings UI. This is the frame that is
     * displayed when
     * the user clicks on the settings tray icon. The frame is created lazily the
     * first time this
     * method is called.
     *
     * @return the settings frame
     */
    public static JFrame getSettingsFrame() {
        return settingsFrame;
    }
}
