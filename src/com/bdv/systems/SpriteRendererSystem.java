package com.bdv.systems;

import com.bdv.ECS.Entity;
import com.bdv.ECS.System;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TransformComponent;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SpriteRendererSystem extends System {
    private BufferedImage image;
    private int width;
    private int height;
    private int[] pixels;
    private int background = 0x892D6F;

    public SpriteRendererSystem() {

    }

    public SpriteRendererSystem(int width, int height) {
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

    @Override
    public void update(Object... objects) {


    }

    public void render(BufferStrategy buffer) {
        Graphics display = buffer.getDrawGraphics();
        display.setColor(new Color(background));
        display.fillRect(0, 0, width, height);

        for (Entity entity : getEntities()) {

            try {

                SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
                TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

                display.drawImage(spriteComponent.image,
                        (int) transformComponent.position.x,
                        (int) transformComponent.position.y,
                        spriteComponent.getWidth(),
                        spriteComponent.getHeight(),
                        null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        display.dispose();
        buffer.show();
    }
}
