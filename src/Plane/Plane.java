package Plane;

import java.awt.image.BufferedImage;

public abstract class Plane {
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    private volatile int x;
    private volatile int y;
    private volatile int width;
    private volatile int height;
    private volatile boolean moving;
    private volatile int direction;     // one of UP, RIGHT, DOWN, LEFT

    public Plane(int x, int y, int width, int height, boolean moving) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.moving = moving;
        this.direction = UP;
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
