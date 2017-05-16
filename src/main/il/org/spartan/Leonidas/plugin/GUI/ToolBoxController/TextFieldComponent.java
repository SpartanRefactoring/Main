package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anna Belozovsky
 * @since 16/05/2017
 */
public class TextFieldComponent extends Component {
    JLabel label;
    JTextField textField;


    void pressed() {
        textField.requestFocus();
    }
}
