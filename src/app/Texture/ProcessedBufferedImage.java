package app.Texture;

import app.Math.RGBAf;

import java.awt.image.BufferedImage;

public class ProcessedBufferedImage {
    private BufferedImage image;
    private int textureId;
    private String pathToFile;

    public ProcessedBufferedImage(BufferedImage image, int textureId, String pathToFile, RGBAf rgb) {
        this.image = image;
        this.textureId = textureId;
        this.pathToFile = pathToFile;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getTextureId() {
        return textureId;
    }

    public String getPathToFile() {
        return pathToFile;
    }
}
