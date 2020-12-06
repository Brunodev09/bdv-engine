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

public class TestGL extends BdvScript {

    public static void main(String[] args) {
        try {
            new Main(TestGL.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InvalidInstance {
        rendererAPI = RendererAPI.OPENGL_RENDERER;
        projectDimensionNumber = ProjectDimensionNumber.twoDimensions;
        logFps = true;

        manager.addSystem(MeshRendererSystem.class);

        Entity base = manager.createEntity();
        TextureComponent.bindAssetsStore(assetPool);

        base.addComponent(TextureComponent.class, "test", "images/grey.png");
        TextureComponent textureComponent = base.getComponent(TextureComponent.class);
        base.addComponent(SpriteComponent.class, textureComponent);
        base.addComponent(TransformComponent.class,
                new Vector3f(500,200, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1),
                new Dimension(100, 100));

        MeshRendererSystem renderer = (MeshRendererSystem) manager.getSystem(MeshRendererSystem.class);
        renderer.addEntity(base);
    }

    @Override
    public void update(double deltaTime) {
//        MeshRendererSystem renderer = (MeshRendererSystem) manager.getSystem(MeshRendererSystem.class);
//        List<Entity> entityList = renderer.getEntities();
//        for (Entity entity : entityList) {
//            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
//            transformComponent.rotation = new Vector3f(0, transformComponent.rotation.y + 0.5f, transformComponent.rotation.z + 0.5f);
//        }
    }
}
