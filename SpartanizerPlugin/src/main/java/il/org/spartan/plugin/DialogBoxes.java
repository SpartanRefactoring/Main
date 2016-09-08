package il.org.spartan.plugin;

import javax.swing.*;

/** A class to manage dialog boxes and other GUI elements.
 * @author Yossi Gil
 * @since 2015-08-27 */
public class DialogBoxes {
  private static final String NAME = "Spartanization";
  private static final String ICON_PATH = "/src/main/icons/spartan-warrior64.gif";
  private static final ImageIcon icon = new ImageIcon(new DialogBoxes().getClass().getResource(ICON_PATH));

  /** @param message What to announce
   * @return <code><b>null</b></code> */
  public static Void announce(final Object message) {
    JOptionPane.showMessageDialog(null, message, NAME, JOptionPane.INFORMATION_MESSAGE, icon);
    return null;
  }
}
