package Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FinalPanel extends JPanel {
    private BufferedImage seaImage;
    private int score;

    public FinalPanel(int score) {
        this.score = score;
        try {
            seaImage = ImageIO.read(new File("Images/Sea.png"));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(seaImage,0,0,640,640,null);
        g.setColor(new Color(0, 35, 87));
        g.setFont(new Font("TimesNewRoman", Font.BOLD, 35));
        g.drawString("Your score is " + score + "...", 150, 250);
    }
}
