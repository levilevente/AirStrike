package Panels;

import java.awt.*;

public class FinalPanel extends BackgroundPanel {
    private int score;

    public FinalPanel(int score) {
        this.score = score;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 35, 87));
        g.setFont(new Font("Times New Roman", Font.BOLD, 35));
        g.drawString("Your score is " + score + "...", 150, 250);
    }
}
