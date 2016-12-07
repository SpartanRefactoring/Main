package il.org.spartan.plugin.demos;

import javax.swing.*;

public class CreateDialogFromOptionPane {
  public static void main(final String[] __) {
    final JFrame parent = new JFrame();
    final JButton button = new JButton();
    button.setText("Click me to show dialog!");
    parent.add(button);
    parent.pack();
    parent.setVisible(true);
    button.addActionListener(evt -> JOptionPane.showInputDialog(parent, "What is your name?", null));
  }
}