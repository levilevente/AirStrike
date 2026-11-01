package Panels;

import Util.Images;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ControlsPanel extends BackgroundPanel {
    private final BufferedImage controlsImage = Images.load("Images/Keys.png");

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(controlsImage,170,300,300,100,null);
    }
}
