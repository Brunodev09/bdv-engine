package samples;

import com.bdv.ECS.Entity;
import com.bdv.Main;
import com.bdv.api.BdvScript;
import com.bdv.api.ProjectDimensionNumber;
import com.bdv.api.RendererAPI;
import com.bdv.components.*;
import com.bdv.exceptions.InvalidInstance;
import com.bdv.systems.MeshRendererSystem;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TestGL3D extends BdvScript {

    public static void main(String[] args) {
        try {
            new Main(TestGL3D.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init()
            throws NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException,
            InvalidInstance {

        rendererAPI = RendererAPI.OPENGL_RENDERER;
        projectDimensionNumber = ProjectDimensionNumber.threeDimensions;

        manager.addSystem(MeshRendererSystem.class);

        Entity base = manager.createEntity();
        TextureComponent.bindAssetsStore(assetPool);

        base.addComponent(ObjComponent.class, "obj/test");
        base.addComponent(TextureComponent.class, "full_spritesheet", "images/grey.png");
        TextureComponent textureComponent = base.getComponent(TextureComponent.class);
        base.addComponent(SpriteComponent.class, textureComponent);
        base.addComponent(TransformComponent.class,
                new Vector3f(0, 0, 0),
                new Vector3f(0.5f, 1, 1),
                new Vector3f(1, 1, 1),
                new Dimension(100, 100));
        base.addComponent(OpenGLReflectivityComponent.class, 1);
        base.addComponent(OpenGLShineDumperComponent.class, 10);

        Entity light = manager.createEntity();
        light.addComponent(OpenGLightsourceComponent.class, new Vector3f(300, 300, -30), new Vector3f(1, 1, 1));

        MeshRendererSystem renderer = (MeshRendererSystem) manager.getSystem(MeshRendererSystem.class);
        renderer.addEntity(base);
        renderer.addEntity(light);
    }

    @Override
    public void update(double deltaTime) {
        MeshRendererSystem renderer = (MeshRendererSystem) manager.getSystem(MeshRendererSystem.class);
        List<Entity> entityList = renderer.getEntities();
        for (Entity entity : entityList) {
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
            if (transformComponent != null)
                transformComponent.rotation = new Vector3f(0, transformComponent.rotation.y + 0.5f, transformComponent.rotation.z);
        }
    }
}
