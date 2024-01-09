package Missile;

import Panels.Sea;
import ShipPackage.Ship;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MissileController implements Runnable{
    private Missile missile;
    private Sea sea;

    public MissileController(Missile missile, Sea sea) {
        this.missile = missile;
        this.sea = sea;
    }

    @Override
    public void run() {
        while (missile.getActive() > 0){
            if (missile.getActive() == 2){
                missile.setExplosion(1);

                List <Ship> ships = sea.getShips();
                for(int i = 0; i < ships.size(); i++){
                    int xExplosion = missile.getX();
                    int yExplosion = missile.getY();
                    if ((xExplosion >= ships.get(i).getX()) && (xExplosion <= (ships.get(i).getX() + ships.get(i).getWidth())) &&
                            (yExplosion >= ships.get(i).getY()) && (yExplosion <= (ships.get(i).getY() + ships.get(i).getHeight()))
                            && !sea.getScoreShowing()){
                        //System.out.println("talalta: "+ ships.get(i).getClass().getSimpleName());
                        //System.out.println(missile.getClass().getSimpleName() + " (" + missile.getX() + " " + missile.getY() + ")");
                        missile.setExplosion(0);
                        sea.modifyShips(i);     //elhelyezi a map vegere es a main megoldja
                        missile.setHit(1);
                    }
                }
                String soundFile;

                try{
                    BufferedImage explosion;
                    if (missile.getHit() == 1){
                        explosion = ImageIO.read(new File("Images/EffectImages/Explosion.png"));
                        soundFile = "Sound/PositiveExplosion.wav";
                        playSound(soundFile, -10.0f);

                    }else{
                        explosion = ImageIO.read(new File("Images/EffectImages/WaterExplosion.png"));
                        soundFile = "Sound/NegativeExplosion.wav";
                        playSound(soundFile, 0);

                    }
                    missile.setImage(explosion);
                    missile.setWidth(20);
                    missile.setHeight(20);
                    missile.setX(missile.getX() - 5);
                    missile.setY(missile.getY() - 5);
                }catch (Exception e){
                    System.out.println("The missile image can't be opened!");
                }
            }
            missile.setActive(missile.getActive() - 1);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.getMessage();
            }
        }
    }

    private static void playSound(String soundFile, float volume) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            gainControl.setValue(volume);

            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
