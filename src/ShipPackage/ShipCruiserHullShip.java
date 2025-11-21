package ShipPackage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ShipCruiserHullShip extends Ship {
    private BufferedImage image;

    public ShipCruiserHullShip(int x, int y, int width, int height, int zone) {
        super(x, y, width, height, zone, 25);
        try{
            image = ImageIO.read(new File("Images/ShipImages/ShipCruiserHull.png"));
        }catch (Exception e){
            System.out.println("The battleship image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
