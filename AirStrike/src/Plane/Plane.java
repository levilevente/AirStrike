package Plane;

import java.awt.image.BufferedImage;

public abstract class Plane {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean moving;


    private int direction;
/*
    0 -> forward
    1  -> right
    2  -> reverse
    3  -> left
 */


    public Plane(int x, int y, int width, int height, boolean moving) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.moving = moving;
        this.direction = 0;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean getMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public abstract BufferedImage getImage();

    public abstract void setImage(BufferedImage image);
}
