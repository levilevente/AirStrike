package Missile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MissileModel extends Missile {
    private BufferedImage image;

    public MissileModel(int x, int y, int width, int height, int active) {
        super(x, y, width, height, active);
        try{
            image = ImageIO.read(new File("Images/Missile.png"));
        }catch (Exception e){
            System.out.println("The missile image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
