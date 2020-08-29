package engine.core;

import engine.texture.*;
import prefabs.Prefab;
import engine.api.EntityAPI;
import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.entities.Entity;
import engine.entities.Lightsource;
import engine.models.BufferedModel;
import engine.models.Model;
import engine.models.TexturedModel;
import engine.video.ModelParser;
import engine.video.Pipeline;
import engine.video.RenderManager;
import org.lwjgl.util.vector.Vector3f;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

// @TODO - Set this up as a gradle project and remove dependencies from commits

public class Engine {

    Pipeline pipe = new Pipeline();
    Map<Integer, Entity> toRender = new HashMap<>();
    Map<Integer, Lightsource> lights = new HashMap<>();
    Map<String, Integer> textures = new HashMap<>();
    Map<SpriteSheet, Integer> sprites = new HashMap<>();
    List<Model> models = new ArrayList<>();
    List<EntityAPI> scriptEntities;
    Map<String, ProcessedBufferedImage> processedImages = new HashMap<>();
    double lastTime;

    Logger LOG = Logger.getLogger(Engine.class.getName());

    public void loop(Configuration config) {

        if (config.script == null) return;

        RenderManager.createRender(config.WIDTH, config.HEIGHT, config.TITLE, config.script.background);
        scriptEntities = config.script.entities;

        if (config.script.camera == null) {

            Camera2D cam2d = config.script.camera2d;


            for (EntityAPI entity : scriptEntities) {

                Model mdl = null;

                if (entity.getWidth() == 0) entity.setWidth(50.0f);
                if (entity.getHeight() == 0) entity.setHeight(50.0f);

                BufferedModel defaultData2D = new BufferedModel(
                        Prefab.squareFactory(entity.getPosition().getX(),
                                entity.getPosition().getY(),
                                entity.getWidth(),
                                entity.getHeight()),
                        Prefab.SquareTextureCoordinates,
                        Prefab.SquareIndexes);

                mdl = pipe.loadDataToVAO(defaultData2D.getVertices(), defaultData2D.getTextures(), defaultData2D.getIndexes());
                models.add(mdl);

                int textureId = getTextureId(entity);
                int newTexure = checkForImageProcessing(entity, textureId);

                if (newTexure != 0) textureId = newTexure;

                ModelTexture texture2D = new ModelTexture(textureId);
                if (entity.getAmbientLight() != null) texture2D.setAmbientLight(entity.getAmbientLight());
                if (entity.getRgbVector() != null) texture2D.setColorOffset(entity.getRgbVector());
                if (entity.isGlowing()) {
                    texture2D.setToggleGlow(true);
                    texture2D.setGlowColor(entity.getColorGlow());
                }
                TexturedModel tmdl2 = new TexturedModel(mdl, texture2D);
                Entity formerEntity = new Entity(tmdl2,
                        entity.getPosition(),
                        entity.getRotationX(),
                        entity.getRotationY(),
                        entity.getRotationZ(),
                        entity.getScaleX(),
                        entity.getScaleY(),
                        entity.getScaleZ());
                entity.setLink(formerEntity.getId());
                toRender.put(formerEntity.getId(), formerEntity);
            }

            while (!RenderManager.shouldExit()) {
                double thisTime = currentTimeMillis();
                double delta = thisTime - lastTime;
                lastTime = thisTime;

                createAndUpdateFormerEntity();
                config.script.update();
                cam2d.move(delta);

                toRender.forEach((key, val) -> RenderManager.processEntity(val));

                RenderManager.renderBatch(cam2d);
                RenderManager.updateRender(config.FPS);
            }

        } else {
            Camera cam = config.script.camera;
            Lightsource light = new Lightsource(new Vector3f(300, 300, -30), new Vector3f(1, 1, 1));

            for (EntityAPI entity : scriptEntities) {
                BufferedModel data = ModelParser.parseOBJ(entity.getModel());
                Model mdl = pipe.loadDataToVAO(data.getVertices(), data.getTextures(), data.getNormals(), data.getIndexes());
                ModelTexture texture = new ModelTexture(pipe.loadTexture(entity.getFile()));
                texture.setShineDamper(10);
                texture.setReflectivity(1);
                TexturedModel tmdl2 = new TexturedModel(mdl, texture);
                Entity formerEntity = new Entity(tmdl2,
                        entity.getPosition(),
                        entity.getRotationX(),
                        entity.getRotationY(),
                        entity.getRotationZ(),
                        entity.getScaleX(),
                        entity.getScaleY(),
                        entity.getScaleZ());
                entity.setLink(formerEntity.getId());
                toRender.put(formerEntity.getId(), formerEntity);
            }

            while (!RenderManager.shouldExit()) {
                double thisTime = currentTimeMillis();
                double delta = thisTime - lastTime;
                lastTime = thisTime;

                config.script.update();
                createAndUpdateFormerEntity();
                cam.move(delta);

                toRender.forEach((key, val) -> RenderManager.processEntity(val));

                RenderManager.renderBatch(light, cam);
                RenderManager.updateRender(config.FPS);
            }
        }

        RenderManager.runCollector();
        pipe.runCollector();
        RenderManager.closeRender();
    }

    private void createAndUpdateFormerEntity() {
        // @TODO - Reflect 2-ways the changes made Entity <-> EntityAPI

        for (EntityAPI entity : scriptEntities) {
            Entity former = toRender.get(entity.getLink());

            if (former.getPosition().x != entity.getPosition().x ||
                    former.getPosition().y != entity.getPosition().y ||
                    former.getPosition().z != entity.getPosition().z) {
                former.setPosition(entity.getPosition());
            }
            if (former.getRotX() != entity.getRotationX()
                    || former.getRotY() != entity.getRotationY()
                    || former.getRotZ() != entity.getRotationZ()) {
                former.setRotX(entity.getRotationX());
                former.setRotY(entity.getRotationY());
                former.setRotZ(entity.getRotationZ());
            }
            if (entity.getEditModel()) {
                int textureId = getTextureId(entity);
                ModelTexture texture2D = new ModelTexture(textureId);
                if (entity.getRgbVector() != null) texture2D.setColorOffset(entity.getRgbVector());
                if (entity.getAmbientLight() != null) texture2D.setAmbientLight(entity.getAmbientLight());
                if (entity.isGlowing()) {
                    texture2D.setToggleGlow(true);
                    texture2D.setGlowColor(entity.getColorGlow());
                }
                TexturedModel tmdl2 = new TexturedModel(models.get(entity.getId() - 1), texture2D);
                former.setModel(tmdl2);
                entity.setEditModel(false);
            }
        }
    }

    // TODO - Make a test with a controlled number of sprites and assert that the texture map is holding that exact controlled number
    private int getTextureId(EntityAPI entity) {
        int id = 0;
        if (textures.get(entity.getFile()) == null || entity.getSpriteSheet() != null) {
            if (entity.getSpriteSheet() != null && sprites.get(entity.getSpriteSheet()) == null) {
                id = pipe.loadTextureFromSpritesheet(entity.getSpriteSheet());
                sprites.put(entity.getSpriteSheet(), id);
            } else if (entity.getSpriteSheet() != null && sprites.get(entity.getSpriteSheet()) != null) {
                id = sprites.get(entity.getSpriteSheet());
            } else if (entity.getSpriteSheet() == null && textures.get(entity.getFile()) == null) {
                id = pipe.loadTexture(entity.getFile());
                textures.put(entity.getFile(), id);
            }
        } else {
            id = textures.get(entity.getFile());
        }
        return id;
    }

    private int checkForImageProcessing(EntityAPI entityAPI, int textureId) {
        // TODO - Setting this to 0 is dangerous
        int resultingTextureId = 0;
        if (entityAPI.getRgb() != null && entityAPI.getEditModel()) {

            entityAPI.setEditModel(false);
            BufferedImage image = TextureCache.getImage(textureId);

            if (image != null) {
                String key = entityAPI.getRgb().toString();
                ProcessedBufferedImage sameProcessed = processedImages.get(key);
                if (sameProcessed != null) {
                    // there is already a texture associated with this image processing work
                    resultingTextureId = sameProcessed.getTextureId();
                } else {
                    // new texture must be generated
                    if (entityAPI.getSpriteSheet() != null) {
                        ImageProcessor.filterImage(image, entityAPI.getRgb(), entityAPI.getSpriteSheet());
                        resultingTextureId = pipe.generateTexture(image, entityAPI.getSpriteSheet());
                    } else {
                        ImageProcessor.filterImage(image, entityAPI.getRgb());
                        resultingTextureId = pipe.generateTexture(image);
                    }
                    ProcessedBufferedImage processed = new ProcessedBufferedImage(image,
                            resultingTextureId,
                            entityAPI.getFile(),
                            entityAPI.getRgb());

                    processedImages.putIfAbsent(entityAPI.getRgb().toString(), processed);
                }
            }

        }
        return resultingTextureId;
    }

    public static double currentTimeMillis() {
        return glfwGetTime() * 1000;
    }

}
