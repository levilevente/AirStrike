package Plane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class PlaneModel extends Plane{

    private BufferedImage image;

    public PlaneModel(int x, int y, int width, int height) {
        super(x, y, width, height, true);
        try{
            image = ImageIO.read(new File("Images/Plane.png"));
        }catch (Exception e){
            System.out.println("The plane image can't be opened!");
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
