import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class App extends JPanel {
    private final JComboBox<String> mainDropdown;
    private final JComboBox<String> holdDropdown;
    private final JLabel holdDropdownLabel;
    private final JComboBox<String> enableDropdown;
    private final JLabel enableDropdownLabel;
    private final JComboBox<String> disableDropdown;
    private final JLabel disableDropdownLabel;
    private final JButton updateValuesButton;
    private final KeyChecker keyChecker;

    public App() {
        keyChecker = new KeyChecker();

        keyChecker.setPushKey("a");
        keyChecker.setEnableKey("a");
        keyChecker.setDisableKey("a");

        addKeyListener(keyChecker);
        setFocusable(true);
        requestFocusInWindow();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Main dropdown for selecting mode
        String[] mainOptions = {"Hold to Auto Click", "Enable/Disable Auto Click"};
        mainDropdown = new JComboBox<>(mainOptions);
        mainDropdown.addActionListener(e -> updateUIForSelectedMode());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(mainDropdown, gbc);

        // Dropdown for "Hold to Auto Click"
        holdDropdownLabel = new JLabel("Hold to Click");
        String[] holdOptions = {"a", "b", "c", "d", "e", "g", "h", "i"};
        holdDropdown = new JComboBox<>(holdOptions);

        // Dropdowns for "Enable/Disable Auto Click"
        enableDropdownLabel = new JLabel("Enable Clicking");
        String[] enableOptions = {"a", "b", "c", "d", "e", "g", "h", "i"};
        enableDropdown = new JComboBox<>(enableOptions);
        
        disableDropdownLabel = new JLabel("Disable Clicking");
        String[] disableOptions = {"a", "b", "c", "d", "e", "g", "h", "i"};
        disableDropdown = new JComboBox<>(disableOptions);

        updateValuesButton = new JButton("Update Values");

        // Add action listener to the update button
        updateValuesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHoldAction = (String) holdDropdown.getSelectedItem();
                keyChecker.setPushKey(selectedHoldAction);

                String selectedEnableAction = (String) enableDropdown.getSelectedItem();
                keyChecker.setEnableKey(selectedEnableAction);

                String selectedDisableAction = (String) disableDropdown.getSelectedItem();
                keyChecker.setDisableKey(selectedDisableAction);

                System.out.println("Values updated: PushKey=" + selectedHoldAction + ", EnableKey=" + selectedEnableAction + ", DisableKey=" + selectedDisableAction);
            }
        });

        // Initialize the UI with the default mode
        updateUIForSelectedMode();
    }

    private void updateUIForSelectedMode() {
        String selectedMode = (String) mainDropdown.getSelectedItem();
        removeAll(); // Clear existing components

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(mainDropdown, gbc); // Re-add main dropdown

        if ("Hold to Auto Click".equals(selectedMode)) {
            keyChecker.onPushMode(true);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(holdDropdownLabel, gbc);
            gbc.gridx = 2;
            gbc.gridy = 1;
            add(holdDropdown, gbc);
        } else if ("Enable/Disable Auto Click".equals(selectedMode)) {
            keyChecker.onPushMode(false);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(enableDropdownLabel, gbc);
            gbc.gridx = 2;
            gbc.gridy = 1;
            add(enableDropdown, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            add(disableDropdownLabel, gbc);
            gbc.gridx = 2;
            gbc.gridy = 2;
            add(disableDropdown, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(updateValuesButton, gbc);

        revalidate(); // Refresh the panel
        repaint(); // Repaint the panel
        requestFocusInWindow(); // Ensure focus back to the panel
    }
}