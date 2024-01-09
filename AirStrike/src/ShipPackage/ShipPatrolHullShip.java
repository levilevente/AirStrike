package ShipPackage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ShipPatrolHullShip extends Ship {
    private BufferedImage image;

    public ShipPatrolHullShip(int x, int y, int width, int height, int zone) {
        super(x, y, width, height, zone, 15);
        try{
            image = ImageIO.read(new File("Images/ShipImages/ShipPatrolHull.png"));
        }catch (Exception e){
            System.out.println("The patrol image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
