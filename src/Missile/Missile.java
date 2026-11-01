package Missile;

import java.awt.image.BufferedImage;

public abstract class Missile {
    private volatile int x;
    private volatile int y;
    private volatile int width;
    private volatile int height;
    private volatile int active;    // seconds left: it explodes at 2 and disappears at 0
    private volatile boolean hit;

    public Missile(int x, int y, int width, int height, int active) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = active;
        this.hit = false;
    }

    public boolean getHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public abstract BufferedImage getImage();

    public abstract void setImage(BufferedImage image);
}
