package com.bdv.components;

import com.bdv.ECS.Component;
import com.bdv.assets.AssetPool;
import com.bdv.exceptions.ComponentException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class TextureComponent extends Component<TextureComponent> {
    public BufferedImage image;
    public String filePath;
    public static AssetPool pool;
    private static boolean _initialized = false;

    public static void bindAssetsStore(AssetPool pool) {
        TextureComponent.pool = pool;
        _initialized = true;
    }

    public static TextureComponent invoke(String id, String filePath) throws ComponentException {
        if (!_initialized) throw new ComponentException("AssetStore must be bound to at least one texture so that this component can function properly.");
        TextureComponent component = new TextureComponent();
        component.filePath = filePath;
        try {
            component.image = ImageIO.read(Objects.requireNonNull(TextureComponent.class.getClassLoader().getResourceAsStream(filePath)));
            pool.addTexture(component, id, filePath);
            return component;
        } catch (Exception e) {
            component.logger.info("ERROR: Could not load file: " + filePath);
            return null;
        }
    }

    public static TextureComponent invoke(String id, BufferedImage image) throws ComponentException {
        if (!_initialized) throw new ComponentException("AssetStore must be bound to at least one texture so that this component can function properly.");
        TextureComponent component = new TextureComponent();
        component.image = image;
        pool.addTexture(component, id, "FROM_IMAGE");
        return component;
    }
}
