package Plane;

import Util.Images;

import java.awt.image.BufferedImage;

public class PlaneModel extends Plane{

    private volatile BufferedImage image;

    public PlaneModel(int x, int y, int width, int height) {
        super(x, y, width, height, true);
        image = Images.load("Images/Plane.png");
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
