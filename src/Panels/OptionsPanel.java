package Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class OptionsPanel extends JPanel {
    private BufferedImage seaImage;
    public OptionsPanel() {
        try {
            seaImage = ImageIO.read(new File("Images/Sea.png"));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(seaImage,0,0,640,640,null);
    }
}