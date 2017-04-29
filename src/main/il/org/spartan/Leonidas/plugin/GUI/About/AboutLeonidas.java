package il.org.spartan.Leonidas.plugin.GUI.About;

import javax.swing.*;
import java.awt.*;


/**
 * @author Amir Sagiv
 * @since 28/04/2017
 */
public class AboutLeonidas extends JFrame{
    private JPanel panel1;
    private JTextPane textPane1;
    private JButton closeButton;

    public AboutLeonidas() {
        super("About Leonidas Plugin");
        setContentPane(panel1);
        setPreferredSize(new Dimension(1000, 600));
        setResizable(false);
        pack();
        setVisible(true);
        closeButton.addActionListener(e -> closeButtonListener());
        textPane1.setFont(textPane1.getFont().deriveFont(16f));
        textPane1.setText("Welcome to IntelliJ Leonidas Plugin.\n\n" +
                "This Plugin was originally developed by 7 students from the Israeli institute of technology - The Technion.\n" +
                "The plugin offers you a tool to apply the principles of Spartan Programming to your Java code.\n" +
                "It applies many different tippers, which are little rules that provide suggestions on how to shorten and simplify your code,e.g, by using fewer variables, factoring out common structures, more efficient use of control flow, etc.\n" +
                "\nThe plugin will help you make a sequence small, nano-refactorings of your code, to make it shorter, and more conforming to a language of nano-patterns.\n" +
                "The resulting code is not just shorter, it is more regular. The spartanization process tries to remove as many distracting details and variations from the code,\n" +
                "stripping it to its bare bone.\n\n" +
                "The plugin is based on the PSI library of IntelliJ-IDEA that allowed us to analyze java code by creating Abstract Syntax Trees from existing code and make changes in these trees.\n" +
                "Each tipper in this plugin was written in a language that we all designed called - Leonidas.\n" +
                "Using this language we are able to create code transformations only by writing an example of generic code that represents the 'before' and 'after' states of these transformation" +
                "\n\n" +
                "In addition to the tipping mechanism, We offer you to enjoy our user services which can be found under 'Leonidas' section in the menu bar of IntelliJ.\n" +
                "There are currently 3 services available:\n" +
                "(1) ToolBox Controller - Decide the tippers you want to be notified about.\n" +
                "(2) Playground - experience the Spartanizer tippers in an isolated environment without effecting yuour code.\n" +
                "(3) Tipper creator - easily create new tippers by code examples without knowing Leonidas language at all!" +
                "\n\nWe encourage you all to join us in developing and enjoying the power of our plugin and the Leonidas language.\n\n" +
                "Leonidas Team.");
    }

    private void closeButtonListener() {
        this.dispose();
    }
}
