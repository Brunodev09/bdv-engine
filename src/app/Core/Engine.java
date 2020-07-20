package app.Core;

import Prefabs.Prefab;
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
    public static void loop(Configuration config) {

        RenderManager.createRender(config.WIDTH, config.HEIGHT, config.TITLE, config.script.background);
        Pipeline pipe = new Pipeline();
        List<EntityAPI> scriptEntities = config.script.entities;
        Map<Integer, Entity> toRender2D = new HashMap<>();
        Map<Integer, Entity> toRender3D = new HashMap<>();
        Map<Integer, Lightsource> lights = new HashMap<>();

        if (config.script.camera == null) {

            Camera2D cam2d = config.script.camera2d;

            BufferedModel defaultData2D = new BufferedModel(Prefab.Square, Prefab.SquareTextureCoordinates, Prefab.SquareIndexes);
            Model mdl = pipe.loadDataToVAO(defaultData2D.getVertices(), defaultData2D.getTextures(), defaultData2D.getIndexes());
            for (EntityAPI entity : scriptEntities) {
                ModelTexture texture2D = new ModelTexture(pipe.loadTexture(entity.getFile()));
                TexturedModel tmdl2 = new TexturedModel(mdl, texture2D);
                Entity formerEntity = new Entity(tmdl2,
                        entity.getPosition(),
                        entity.getRotationX(),
                        entity.getRotationY(),
                        entity.getRotationZ(),
                        entity.getScale());
                entity.setLink(formerEntity.getId());
                toRender2D.put(formerEntity.getId(), formerEntity);
            }

            while (!RenderManager.shouldExit()) {
                config.script.update();
                _updateFormerEntities(config.script.entities, toRender2D);
                cam2d.move();

                toRender2D.forEach((key, val) -> {
                    RenderManager.processEntity(val);
                });

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
                toRender3D.put(formerEntity.getId(), formerEntity);
            }

            while (!RenderManager.shouldExit()) {
                config.script.update();
                _updateFormerEntities(config.script.entities, toRender3D);
                cam.move();

                toRender3D.forEach((key, val) -> {
                    RenderManager.processEntity(val);
                });

                RenderManager.renderBatch(light, cam);
                RenderManager.updateRender(config.FPS);
            }
        }

        RenderManager.runCollector();
        pipe.runCollector();
        RenderManager.closeRender();
    }

    private static void _updateFormerEntities(List<EntityAPI> entities, Map<Integer, Entity> formerEntities) {
        for (EntityAPI entity : entities) {
            Entity former = formerEntities.get(entity.getLink());

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
        }
    }
}
