package engine.video;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final Map<String, Image> imageCache = new HashMap<>();

    private TextureCache() {}

    public static Image loadImage(String absolutePath) {
        try {
            if (imageCache.get(absolutePath) != null) return imageCache.get(absolutePath);
            Image image = ImageIO.read(new File(absolutePath + ".png"));
            imageCache.put(absolutePath, image);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image getImageFromCache(String path) {
        return imageCache.get(path);
    }
}
