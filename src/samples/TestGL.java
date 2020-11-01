package samples;

import com.bdv.ECS.Entity;
import com.bdv.Main;
import com.bdv.api.BdvScript;
import com.bdv.api.ProjectDimensionNumber;
import com.bdv.api.RendererAPI;
import com.bdv.components.ObjComponent;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TextureComponent;
import com.bdv.components.TransformComponent;
import com.bdv.exceptions.InvalidInstance;
import com.bdv.systems.MeshRendererSystem;
import com.bdv.systems.MeshTerrainRendererSystem;
import com.bdv.systems.SpriteRendererSystem;
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
        projectDimensionNumber = ProjectDimensionNumber.threeDimensions;

        manager.addSystem(MeshRendererSystem.class);

        Entity base = manager.createEntity();
        TextureComponent.bindAssetsStore(assetPool);

        base.addComponent(ObjComponent.class, "obj/test");
        base.addComponent(TextureComponent.class, "full_spritesheet", "images/assetsComplete.png");
        TextureComponent textureComponent = base.getComponent(TextureComponent.class);
        base.addComponent(SpriteComponent.class, textureComponent);
        base.addComponent(TransformComponent.class,
                new Vector3f(0, 0, 0),
                new Vector3f(0.5f, 1, 1),
                new Vector3f(1, 1, 1));

        MeshRendererSystem renderer = (MeshRendererSystem) manager.getSystem(MeshRendererSystem.class);
        renderer.addEntity(base);
    }

    @Override
    public void update(double deltaTime) {

    }
}
