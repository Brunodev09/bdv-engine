package com.bdv.assets;


import com.bdv.renders.Texture;

import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private Map<String, Texture> textures = new HashMap<>();

    public AssetPool() {

    }

    public void clearPool() {
        textures.clear();
    }

    public void addTexture(Texture texture, String assetId, String filePath) {
        textures.put(assetId, texture);
    }

    public Texture getTexture(String assetId) {
        return textures.get(assetId);
    }

}
