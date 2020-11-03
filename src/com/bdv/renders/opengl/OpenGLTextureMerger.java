package com.bdv.renders.opengl;

import com.bdv.ECS.Entity;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TransformComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class OpenGLTextureMerger {

    private OpenGLTextureMerger() {}

    public static BufferedImage merge(int width, int height, List<Entity> entityList) {
        final BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics2D graphics2D = canvas.createGraphics();
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fillRect(0, 0, width, height);
        Color oldColor = graphics2D.getColor();

        for (Entity entity : entityList) {
            SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

            if (spriteComponent != null &&
                    transformComponent != null &&
                    transformComponent.position.x >= 0 &&
                    transformComponent.position.x <= width &&
                    transformComponent.position.y >= 0 &&
                    transformComponent.position.y <= height) {
                graphics2D.setColor(oldColor);
                graphics2D.drawImage(spriteComponent.image, null, (int) transformComponent.position.x, (int) transformComponent.position.y);
            }
        }

        graphics2D.dispose();
        return canvas;
    }
}
