package engine.texture;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final Map<Integer, BufferedImage> textureIdPerImage = new HashMap<>();

    private TextureCache() {}

    public static BufferedImage getImage(int textureId) {
        return TextureCache.textureIdPerImage.get(textureId);
    }

    public static void addImage(int textureId, BufferedImage image) {
        TextureCache.textureIdPerImage.putIfAbsent(textureId, image);
    }
}
