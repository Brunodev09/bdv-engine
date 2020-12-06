package com.bdv.components;

import com.bdv.ECS.Component;
import com.bdv.assets.AssetPool;
import com.bdv.exceptions.ComponentException;
import com.bdv.exceptions.OpenGLTextureProcessorException;
import com.bdv.renders.opengl.OpenGLTextureProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class TextureComponent extends Component<TextureComponent> {
    public BufferedImage image;
    public String filePath;
    public String id;
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
        component.id = id;
        if (pool.getTexture(id) == null) {
            try {
                component.image = ImageIO.read(Objects.requireNonNull(TextureComponent.class.getClassLoader().getResourceAsStream(filePath)));
                pool.addTexture(component, id, filePath);
                mergeTextureIntoMasterCanvas(id, component.image);
                return component;
            } catch (OpenGLTextureProcessorException | IOException e) {
                component.logger.info("ERROR: Could not load file: " + filePath);
                return null;
            }
        }
        return pool.getTexture(id);
    }

    public static TextureComponent invoke(String id, BufferedImage image) throws ComponentException {
        if (!_initialized) throw new ComponentException("AssetStore must be bound to at least one texture so that this component can function properly.");
        TextureComponent component = new TextureComponent();
        component.image = image;
        component.id = id;
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        pool.addTexture(component, id, "FROM_IMAGE_" + uuid);
        try {
            mergeTextureIntoMasterCanvas(id, component.image);
        } catch (OpenGLTextureProcessorException e) {
            throw new ComponentException(e.getMessage());
        }
        return component;
    }

    private static void mergeTextureIntoMasterCanvas(String id, BufferedImage image) throws OpenGLTextureProcessorException {
        OpenGLTextureProcessor.merge(id, image);
    }
}
