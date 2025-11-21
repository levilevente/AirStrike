package ShipPackage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class CarrierShip extends Ship {
    private BufferedImage image;

    public CarrierShip(int x, int y, int width, int height, int zone) {
        super(x, y, width, height, zone,40);
        try{
            image = ImageIO.read(new File("Images/ShipImages/ShipCarrier.png"));
        }catch (Exception e){
            System.out.println("The carrier image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
