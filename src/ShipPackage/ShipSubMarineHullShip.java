package ShipPackage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ShipSubMarineHullShip extends Ship {
    private BufferedImage image;

    public ShipSubMarineHullShip(int x, int y, int width, int height, int zone) {
        super(x, y, width, height, zone, 40);
        try{
            image = ImageIO.read(new File("Images/ShipImages/ShipSubMarineHull.png"));
        }catch (Exception e){
            System.out.println("The submarine image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
