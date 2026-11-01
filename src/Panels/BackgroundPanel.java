package Panels;

import Util.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// base panel that draws the sea image as background
public abstract class BackgroundPanel extends JPanel {
    private final BufferedImage seaImage = Images.load("Images/Sea.png");

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(seaImage, 0, 0, Sea.SIZE, Sea.SIZE, null);
    }
}
