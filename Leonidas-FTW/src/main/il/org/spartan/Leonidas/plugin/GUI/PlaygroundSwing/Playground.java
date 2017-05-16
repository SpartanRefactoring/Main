package il.org.spartan.Leonidas.plugin.GUI.PlaygroundSwing;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.plugin.Spartanizer;
import il.org.spartan.Leonidas.plugin.Toolbox;

import javax.swing.*;
import java.awt.*;

/**
 * @author Anna Belozovsky
 * @since 22/04/2017
 */
public class Playground extends JFrame {
    private JPanel mainPanel;
    private JButton clearButton;
    private JButton spartanizeButton;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JLabel input;
    private JLabel output;
    private JPanel textPanel;
    private JPanel buttonPanel;
    private JScrollPane inputScroll;
    private JScrollPane outputScroll;
    private String before = "public class foo{ public void main(){\n";
    private String after = "\n}}";

    public Playground() {
        super("Spartanizer Playground");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(600, 600));
        setResizable(false);
        pack();
        setVisible(true);
        outputArea.setEditable(false);
        spartanizeButton.addActionListener(e -> spartanizeButtonClicked());
        clearButton.addActionListener(e -> clearButtonClicked());
    }

    private void spartanizeButtonClicked() {
        PsiFile file = PsiFileFactory.getInstance(Utils.getProject())
                .createFileFromText(JavaLanguage.INSTANCE, before + inputArea.getText() + after);
        Toolbox.getInstance().playground = true;
        Spartanizer.spartanizeFileOnePass(file);
        Toolbox.getInstance().playground = false;
        String temp = file.getText().substring(before.length());
        outputArea.setText(temp.substring(0, temp.length() - after.length()));
    }

    private void clearButtonClicked() {
        outputArea.setText("");
    }
}
