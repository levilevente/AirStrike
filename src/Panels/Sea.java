package Panels;

import Missile.*;
import Plane.*;
import ShipPackage.Ship;
import Timer.*;
import Util.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Sea extends JPanel {
    public static final int SIZE = 640;     // the map is SIZE x SIZE pixels
    private static final Font SCORE_FONT = new Font("Times New Roman", Font.BOLD, 22);

    private List<Ship> ships;
    private List<Missile> missiles;
    private Plane plane;
    private TimerModel timerModel;
    private BufferedImage seaImage;
    private BufferedImage cloudOnTop;
    private volatile boolean scoreShowing;
    private volatile int destroyedShipScore;

    public Sea(List <Ship> ships, Plane plane, List <Missile> missiles, TimerModel timerModel){
        this.plane = plane;
        this.ships = ships;
        this.missiles = missiles;
        this.destroyedShipScore = 0;
        this.scoreShowing = false;
        this.timerModel = timerModel;
        cloudOnTop = Images.load("Images/CloudOnTop.png");
        seaImage = Images.load("Images/Sea.png");
    }

    public int activeMissiles(){
        int s = 0;
        for (Missile m : missiles){
            if (m.getActive() != 0){
                s++;
            }
        }
        return s;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(seaImage,0,0,SIZE,SIZE,null); //draw the background sea
        for (Ship s : ships){       //draw the ships on it
            g.drawImage(s.getImage(), s.getX(), s.getY(), s.getWidth(), s.getHeight(), null);
        }
        g.drawImage(cloudOnTop,0,0,SIZE,100, null);  //draw the cloud on top of the panel
        for (Missile m : missiles){         //the missiles
            if (m.getActive() != 0){
                g.drawImage(m.getImage(), m.getX(), m.getY(), m.getWidth(), m.getHeight(), null);
            }
        }
        g.drawImage(plane.getImage(), plane.getX(), plane.getY(), plane.getWidth(), plane.getHeight(), null);   //the plane

        g.setColor(new Color(0, 7, 87));            //the actual score
        g.setFont(SCORE_FONT);
        g.drawString("Score: " + destroyedShipScore, 10, 20);

        TimerView.drawTime(g, timerModel);
    }
    public List<Ship> getShips() {
        return ships;
    }
    // destroys the ship and adds its points; returns false if the round is over or the ship is already gone
    // synchronized because more missiles can explode at the same time
    public synchronized boolean modifyShips(Ship ship){
        if (scoreShowing || ship.isGone()){
            return false;
        }
        ship.setY(-ship.getHeight());     //the ship is eliminated, the game loop replaces it
        destroyedShipScore += ship.getPoints();
        return true;
    }
    public int getDestroyedShipScore() {
        return destroyedShipScore;
    }
    public boolean getScoreShowing() {
        return scoreShowing;
    }
    // synchronized so no ship can be scored after the final score is read
    public synchronized void setScoreShowing(boolean scoreShowing) {
        this.scoreShowing = scoreShowing;
    }
    public TimerModel getTimerModel() {
        return timerModel;
    }
}
