package il.org.spartan.Leonidas.plugin.GUI.TipperCreator;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anna Belozovsky
 * @since 12/05/2017
 */
public class TipperNameCreator extends JFrame {
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel mainPanel;
    private JTextField nameField;
    private TipperCreator callee;

    TipperNameCreator(TipperCreator callee) {
        super("Name new tipper");
        this.callee = callee;
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(300, 100));
        setResizable(false);
        pack();
        setVisible(true);
        saveButton.addActionListener(e -> saveButtonClicked());
        cancelButton.addActionListener(e -> cancelButtonClicked());
    }

    private void saveButtonClicked() {
        String name = nameField.getText();
        if (name.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            if (getClass().getClassLoader().getResource("il/org/spartan/Leonidas/plugin/tippers/leonidas/" + name + ".java") != null) {
                JOptionPane.showMessageDialog(this, "A tipper with that name already exists.");
                return;
            }
            callee.tipperName = name;
            dispose();
        } else if (name.matches("[0-9][a-zA-Z0-9]*")) {
            JOptionPane.showMessageDialog(this, "A name cannot start with a number.");
        } else if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty");
        } else {
            JOptionPane.showMessageDialog(this, "Only letters and numbers are allowed.");
        }
    }

    private void cancelButtonClicked() {
        dispose();
    }
}
