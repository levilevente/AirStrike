package Timer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TimerView extends JPanel {
    private TimerModel model;
    public TimerView(TimerModel model){
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g){
        if (model.getStart() > 10){
            g.setColor(new Color(0, 87, 0));
            g.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
            g.drawString(model.getStart() + " seconds remaining...", 170,30);
        }else{
            g.setColor(new Color(136, 0, 0));
            g.setFont(new Font("TimesNewRoman", Font.BOLD, 30));
            g.drawString(model.getStart() + "...", 310,30);
        }

    }
}
