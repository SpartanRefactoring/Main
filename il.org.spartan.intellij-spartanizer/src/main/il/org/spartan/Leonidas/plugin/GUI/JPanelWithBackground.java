package il.org.spartan.Leonidas.plugin.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by roym on 27/06/17.
 */
public class JPanelWithBackground extends JPanel {

    private Image backgroundImage;

    // Some code to initialize the background image.
    // Here, we use the constructor to load the image. This
    // can vary depending on the use case of the panel.
    public JPanelWithBackground(String fileName) throws IOException {
        backgroundImage = ImageIO.read(getClass().getResource(fileName));
    }

    public void paintComponent(Graphics ¢) {
        super.paintComponent(¢);

        // Draw the background image.
        ¢.drawImage(backgroundImage, 0, 0, this);
    }
}