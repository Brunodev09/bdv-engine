package com.bdv.components;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

public class RenderComponent {
    private BufferedImage image;
    private int width;
    private int height;
    private int[] pixels;
    private int background;

    public RenderComponent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public void setBackground(int color) {
        background = color;
    }

    public void render(BufferStrategy buffer, List<SpriteComponent> sprites) {
        Graphics display = buffer.getDrawGraphics();
        display.setColor(new Color(background));
        display.fillRect(0, 0, width, height);



        display.dispose();
        buffer.show();
    }
}
