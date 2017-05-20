package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anna Belozovsky
 * @since 16/05/2017
 */
public class TextFieldComponent extends JPanel {
    JLabel label;
    JTextField textField;


    public TextFieldComponent() {
        super(new FlowLayout(FlowLayout.LEFT));
        label = new JLabel("hi");
        textField = new JTextField("", 20);

        label.setVisible(true);
        textField.setVisible(true);
        this.add(label);
        this.add(textField);

        this.setVisible(true);

    }

    void pressed() {
        textField.grabFocus();
    }
}
