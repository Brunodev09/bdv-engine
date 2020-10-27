package com.bdv.assets;


import com.bdv.components.TextureComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AssetPool {
    private Map<String, TextureComponent> textures = new HashMap<>();
    private final Logger logger = Logger.getLogger(AssetPool.class.getName());

    public void clearPool() {
        logger.info("[ASSETS_POOL]: Texture pool cache cleared!");
        textures.clear();
    }

    public void addTexture(TextureComponent texture, String assetId, String filePath) {
        logger.info("[ASSETS_POOL]: Texture loaded with the id of " + assetId + " from file path " + filePath);
        textures.put(assetId, texture);
    }

    public TextureComponent getTexture(String assetId) {
        return textures.get(assetId);
    }

}
