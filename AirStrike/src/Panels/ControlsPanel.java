package Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ControlsPanel extends JPanel {
    private BufferedImage seaImage;
    private BufferedImage controlsImage;
    public ControlsPanel() {
        try {
            controlsImage = ImageIO.read(new File("Images/Keys.png"));
            seaImage = ImageIO.read(new File("Images/Sea.png"));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(seaImage,0,0,640,640,null);
        g.drawImage(controlsImage,170,300,300,100,null);
    }
}
