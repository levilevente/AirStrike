package ShipPackage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ShipRescueShip extends Ship {
    private BufferedImage image;

    public ShipRescueShip(int x, int y, int width, int height, int zone) {
        super(x, y, width, height, zone, 25);
        try{
            image = ImageIO.read(new File("Images/ShipImages/ShipRescue.png"));
        }catch (Exception e){
            System.out.println("The ShipRescueShip image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
