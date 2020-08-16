package app.Core;

import app.Texture.SpriteSheet;
import prefabs.Prefab;
import app.API.EntityAPI;
import app.Entities.Camera;
import app.Entities.Camera2D;
import app.Entities.Entity;
import app.Entities.Lightsource;
import app.Models.BufferedModel;
import app.Models.Model;
import app.Models.TexturedModel;
import app.Texture.ModelTexture;
import app.Video.ModelParser;
import app.Video.Pipeline;
import app.Video.RenderManager;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class Engine {

    Pipeline pipe = new Pipeline();
    Map<Integer, Entity> toRender = new HashMap<>();
    Map<Integer, Lightsource> lights = new HashMap<>();
    Map<String, Integer> textures = new HashMap<>();
    Map<SpriteSheet, Integer> sprites = new HashMap<>();
    List<Model> models = new ArrayList<>();
    List<EntityAPI> scriptEntities;

    public void loop(Configuration config) {

        if (config.script == null) return;

        RenderManager.createRender(config.WIDTH, config.HEIGHT, config.TITLE, config.script.background);
        scriptEntities = config.script.entities;

        if (config.script.camera == null) {

            Camera2D cam2d = config.script.camera2d;

            BufferedModel defaultData2D = new BufferedModel(Prefab.Square, Prefab.SquareTextureCoordinates, Prefab.SquareIndexes);
            Model mdl = pipe.loadDataToVAO(defaultData2D.getVertices(), defaultData2D.getTextures(), defaultData2D.getIndexes());
            models.add(mdl);

            for (EntityAPI entity : scriptEntities) {

                int textureId = getTextureId(entity);

                ModelTexture texture2D = new ModelTexture(textureId);
                TexturedModel tmdl2 = new TexturedModel(mdl, texture2D);
                Entity formerEntity = new Entity(tmdl2,
                        entity.getPosition(),
                        entity.getRotationX(),
                        entity.getRotationY(),
                        entity.getRotationZ(),
                        entity.getScale());
                entity.setLink(formerEntity.getId());
                toRender.put(formerEntity.getId(), formerEntity);
            }

            while (!RenderManager.shouldExit()) {
                createAndUpdateFormerEntity();
                config.script.update();
                cam2d.move();

                toRender.forEach((key, val) -> RenderManager.processEntity(val));

                RenderManager.renderBatch(cam2d);
                RenderManager.updateRender(config.FPS);
            }

        }
        else {
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
                        entity.getScale());
                entity.setLink(formerEntity.getId());
                toRender.put(formerEntity.getId(), formerEntity);
            }

            while (!RenderManager.shouldExit()) {
                config.script.update();
                createAndUpdateFormerEntity();
                cam.move();

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
                // @TODO - Please please, do something about this...
                TexturedModel tmdl2 = new TexturedModel(models.get(0), texture2D);
                former.setModel(tmdl2);
                entity.setEditModel(false);
            }
        }
    }

    private int getTextureId(EntityAPI entity) {
        int id = 0;
        if (textures.get(entity.getFile()) == null || entity.getSpriteSheet() != null) {
            if (entity.getSpriteSheet() != null && sprites.get(entity.getSpriteSheet()) == null) {
                id = pipe.loadTextureFromSpritesheet(entity.getSpriteSheet());
                sprites.put(entity.getSpriteSheet(), id);
                return id;
            }
            else if (entity.getSpriteSheet() != null && sprites.get(entity.getSpriteSheet()) != null) {
                return sprites.get(entity.getSpriteSheet());
            }
            else if (entity.getSpriteSheet() == null && entity.getFile() == null) {
                id = pipe.loadTexture(entity.getFile());
                textures.put(entity.getSpriteSheet().getFile(), id);
            }
        }
        else {
            id = textures.get(entity.getFile());
        }
        return id;
    }
}
