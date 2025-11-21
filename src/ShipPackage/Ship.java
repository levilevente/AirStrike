package ShipPackage;

import java.awt.image.BufferedImage;

public abstract class Ship {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int zone;

    public Ship(int x, int y, int width, int height, int zone, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.zone = zone;
    }
    public int getZone() {
        return zone;
    }
    public void setZone(int zone) {
        this.zone = zone;
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
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public abstract BufferedImage getImage();

}
