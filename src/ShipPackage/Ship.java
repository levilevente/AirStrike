package ShipPackage;

import Util.Images;

import java.awt.image.BufferedImage;

public abstract class Ship {
    private int x;
    private volatile int y;     // changed by the ship thread and the missile threads, read by the game loop
    private int width;
    private int height;
    private int speed;          // sleep time between two moves (ms), so higher is slower
    private int zone;
    private final int points;   // score given when the ship is destroyed
    private final BufferedImage image;

    public Ship(int x, int y, int width, int height, int zone, int speed, int points, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.zone = zone;
        this.points = points;
        this.image = Images.load(imagePath);
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
    public int getPoints() {
        return points;
    }
    // true when the ship left the top of the map or was destroyed
    public boolean isGone() {
        return y <= -height;
    }
    public BufferedImage getImage() {
        return image;
    }
}
