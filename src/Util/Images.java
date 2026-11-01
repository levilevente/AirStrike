package Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Images {
    // images are only read, never changed, so one copy per file is shared
    private static final Map<String, BufferedImage> cache = new ConcurrentHashMap<>();

    private Images() {}

    // returns null (and logs the error) if the image can't be opened
    public static BufferedImage load(String path) {
        BufferedImage image = cache.get(path);
        if (image != null) {
            return image;
        }
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("The image " + path + " can't be opened: " + e.getMessage());
        }
        if (image == null) {
            return null;
        }
        cache.put(path, image);
        return image;
    }

    public static BufferedImage rotate(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);
        at.rotate(rads, w / 2.0, h / 2.0);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
}
