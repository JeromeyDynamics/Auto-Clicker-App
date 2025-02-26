import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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

    public App() {
        addKeyListener(new KeyChecker());
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

        // Initialize the UI with the default mode
        updateUIForSelectedMode();

        holdDropdown.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAction = (String) holdDropdown.getSelectedItem();
                KeyChecker.setPushKey(selectedAction);
            }
        });

        enableDropdown.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAction = (String) enableDropdown.getSelectedItem();
                KeyChecker.setEnableKey(selectedAction);
            }
        });

        disableDropdown.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAction = (String) disableDropdown.getSelectedItem();
                KeyChecker.setDisableKey(selectedAction);
            }
        });
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
            KeyChecker.onPushMode(true);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(holdDropdownLabel, gbc);
            gbc.gridx = 2;
            gbc.gridy = 1;
            add(holdDropdown, gbc);
        } else if ("Enable/Disable Auto Click".equals(selectedMode)) {
            KeyChecker.onPushMode(false);

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

        revalidate(); // Refresh the panel
        repaint(); // Repaint the panel
    }
}
