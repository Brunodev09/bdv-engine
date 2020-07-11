package app.Video;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Texture {
    private String location;
    private int _width;
    private int _height;

    public Texture(String location) {
        this.location = location;
    }

    public String getResourceLocation() {
        return location;
    }

    public void setResourceLocation(String location) {
        this.location = location;
    }

    public int[] getImagePixels(String path) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            _width = image.getWidth();
            _height = image.getHeight();
            pixels = new int[_width * _height];
            image.getRGB(0, 0, _width, _height, pixels, 0, _width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] data = new int[_width * _height];
        for (int i = 0; i < _width * _height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        return data;
    }
}
