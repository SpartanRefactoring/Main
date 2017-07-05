package il.org.spartan.Leonidas.plugin.GUI.LoadingIndicator;

import javax.swing.*;
import java.awt.*;

/**
 * Created by roym on 28/06/17.
 */
public class LoadingIndicator extends JFrame {

    private JLabel loadingMessage;
    private JLabel loadingAnimation;
    private JPanel contentPane;

    public LoadingIndicator(String message) {
        super("");
        loadingMessage.setText(message);
        this.add(contentPane);
        setContentPane(contentPane);
        setPreferredSize(new Dimension(700, 100));
        setResizable(false);
        pack();
        setVisible(true);
    }
}
