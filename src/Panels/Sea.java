package Panels;

import Missile.*;
import Plane.*;
import ShipPackage.Ship;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import Timer.*;

public class Sea extends JPanel {
    private List<Ship> ships;
    private List<Missile> missiles;
    private Plane plane;
    private TimerModel timerModel;
    private BufferedImage seaImage;
    private BufferedImage cloudOnTop;
    private boolean scoreShowing;
    private int destroyedShipScore;

    public Sea(List <Ship> ships, Plane plane, List <Missile> missiles, TimerModel timerModel){
        this.plane = plane;
        this.ships = ships;
        this.missiles = missiles;
        this.destroyedShipScore = 0;
        this.scoreShowing = false;
        this.timerModel = timerModel;
        try{
            cloudOnTop = ImageIO.read(new File("Images/CloudOnTop.png"));
            seaImage = ImageIO.read(new File("Images/Sea.png"));
        } catch (Exception e){
            System.out.println("The sea/cloud image can't be opened!");
        }
    }

    public int activeShips(){
        int s = 0;
        for (Ship i : ships){
            if (i.getY() + i.getHeight() > 0){
                s++;
            }
        }
        return s;
    }
    public int activeMissiles(){
        int s = 0;
        for (Missile i : missiles){
            if (i.getActive() != 0){
                s++;
            }
        }
        return s;
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(seaImage,0,0,640,640,null); //draw the background sea
        for (Ship i : ships){       //draw the ships on it
            g.drawImage(i.getImage(), i.getX(), i.getY(), i.getWidth(), i.getHeight(), null);
        }
        g.drawImage(cloudOnTop,0,0,640,100, null);  //draw the cloud on top of the panel
        for (Missile m : missiles){         //the missiles
            if (m.getActive() != 0){
                g.drawImage(m.getImage(), m.getX(), m.getY(), m.getWidth(), m.getHeight(), null);
            }
        }
        g.drawImage(plane.getImage(), plane.getX(), plane.getY(), plane.getWidth(), plane.getHeight(), null);   //the plane

        g.setColor(new Color(0, 7, 87));            //the actual score
        g.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
        g.drawString("Score: " + String.valueOf(destroyedShipScore), 10, 20);

        //show the time remained (red if <=10 | green >10)
        if (timerModel.getStart() > 10){
            g.setColor(new Color(0, 87, 0));
            g.setFont(new Font("TimesNewRoman", Font.BOLD, 22));
            g.drawString(timerModel.getStart() + " seconds remaining...", 170,30);
        }else{
            g.setColor(new Color(136, 0, 0));
            g.setFont(new Font("TimesNewRoman", Font.BOLD, 30));
            g.drawString(timerModel.getStart() + "...", 310,30);
        }
    }
    public List<Ship> getShips() {
        return ships;
    }
    public void modifyShips(int i){
        if (!scoreShowing){
            Ship oldShip = ships.get(i);
            oldShip.setY(oldShip.getHeight() * -1);     //the ship is eliminated
            if (oldShip.getClass().getSimpleName().equals("CarrierShip")){
                destroyedShipScore += 1;
            } else if (oldShip.getClass().getSimpleName().equals("BattleShip") || oldShip.getClass().getSimpleName()
                    .equals("ShipSubMarineHullShip")) {
                destroyedShipScore += 2;
            } else if (oldShip.getClass().getSimpleName().equals("ShipRescueShip")) {
                destroyedShipScore += 3;
            } else{
                destroyedShipScore += 4;
            }
        }
    }
    public int getDestroyedShipScore() {
        return destroyedShipScore;
    }
    public boolean getScoreShowing() {
        return scoreShowing;
    }
    public void setScoreShowing(boolean scoreShowing) {
        this.scoreShowing = scoreShowing;
    }
    public TimerModel getTimerModel() {
        return timerModel;
    }
}
