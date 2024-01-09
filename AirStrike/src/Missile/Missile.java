package Missile;

import java.awt.image.BufferedImage;

public abstract class Missile {
    private int x;
    private int y;
    private int width;
    private int height;
    private int active;
    private int explosion;
    private int hit;

    public Missile(int x, int y, int width, int height, int active) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = active;
        this.explosion = 0;
        this.hit = 0;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getExplosion() {
        return explosion;
    }

    public void setExplosion(int explosion) {
        this.explosion = explosion;
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
