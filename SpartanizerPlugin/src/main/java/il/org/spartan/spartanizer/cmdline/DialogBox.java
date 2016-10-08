package il.org.spartan.spartanizer.cmdline;

import java.awt.*;

import javax.swing.*;

import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.dialogs.*;

/**
 * @author ?
 * @since 2016 */
public final class DialogBox {
  static JFrame frmOpt;  //dummy JFrame

  public static void main ( String args[] ) 
  { 
    System.out.println("1");
    //JOptionPane.showInputDialog(new JFrame("myFrame"), "HelloWorld");
    //activate();
    //InputDialog(null, "title", "message", "fill me", (s) -> {return s;});
    System.out.println("2");
  } 
 
  private static void activate() {
    if (frmOpt == null)
      frmOpt = new JFrame();
    frmOpt.setVisible(true);
    frmOpt.setLocation(100, 100);
    frmOpt.setAlwaysOnTop(true);
    String[] options = {"delete", "hide", "break"};
    JOptionPane.showOptionDialog(frmOpt, "message", "title", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, "delete");
    frmOpt.dispose();
}
}
