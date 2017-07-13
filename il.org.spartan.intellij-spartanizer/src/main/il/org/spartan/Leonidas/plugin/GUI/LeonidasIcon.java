package il.org.spartan.Leonidas.plugin.GUI;

import javax.swing.*;

/**
 * @author RoeiRaz
 * @since 26/5/17
 * <p>
 * keeps the path to the leonidas icon.
 */
public enum LeonidasIcon {
    ;
    private static final String iconPath = "leonidas.png";

    public static void apply(JFrame f) {
        f.setIconImage(new ImageIcon(LeonidasIcon.class.getClassLoader().getResource(iconPath)).getImage());
    }
}
