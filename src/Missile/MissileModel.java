package Missile;

import Util.Images;

import java.awt.image.BufferedImage;

public class MissileModel extends Missile {
    private volatile BufferedImage image;

    public MissileModel(int x, int y, int width, int height, int active) {
        super(x, y, width, height, active);
        image = Images.load("Images/Missile.png");
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
