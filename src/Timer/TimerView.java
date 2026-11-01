package Timer;

import javax.swing.*;
import java.awt.*;

public class TimerView extends JPanel {
    private static final Font NORMAL_FONT = new Font("Times New Roman", Font.BOLD, 22);
    private static final Font FINAL_SECONDS_FONT = new Font("Times New Roman", Font.BOLD, 30);

    private TimerModel model;
    public TimerView(TimerModel model){
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawTime(g, model);
    }

    // shows the remaining time (green if > 10, red in the last 10 seconds)
    public static void drawTime(Graphics g, TimerModel model){
        int secondsLeft = model.getStart();
        if (secondsLeft > 10){
            g.setColor(new Color(0, 87, 0));
            g.setFont(NORMAL_FONT);
            g.drawString(secondsLeft + " seconds remaining...", 170,30);
        }else{
            g.setColor(new Color(136, 0, 0));
            g.setFont(FINAL_SECONDS_FONT);
            g.drawString(secondsLeft + "...", 310,30);
        }
    }
}
