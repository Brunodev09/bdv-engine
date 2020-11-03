package com.bdv.systems;

import com.bdv.ECS.Entity;
import com.bdv.ECS.System;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TransformComponent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

                if (transformComponent.rotation.x > 0 && transformComponent.rotation.x <= 1.0f) {
                    double rotation = Math.toRadians(360.0 * transformComponent.rotation.x);
                    AffineTransform tx = AffineTransform.getRotateInstance(rotation,
                            spriteComponent.getWidth() / 2.0,
                            spriteComponent.getHeight() / 2.0);
                    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                    display.drawImage(op.filter(spriteComponent.image, null),
                            (int) transformComponent.position.x,
                            (int) transformComponent.position.y,
                            null);
                } else {
                    display.drawImage(spriteComponent.image,
                            (int) transformComponent.position.x,
                            (int) transformComponent.position.y,
                            transformComponent.dimension.getWidth(),
                            transformComponent.dimension.getHeight(),
                            null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        display.dispose();
        buffer.show();
    }
}
