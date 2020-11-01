package samples;

import com.bdv.ECS.Entity;
import com.bdv.Main;
import com.bdv.api.BdvScript;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TextureComponent;
import com.bdv.components.TransformComponent;
import com.bdv.exceptions.InvalidInstance;
import com.bdv.systems.SpriteRendererSystem;
import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.InvocationTargetException;

public class Test extends BdvScript {
    public static void main(String[] args) {
        try {
            new Main(Test.class);
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

        manager.addSystem(SpriteRendererSystem.class);

        Entity base = manager.createEntity();
        TextureComponent.bindAssetsStore(assetPool);

        base.addComponent(TextureComponent.class, "full_spritesheet", "images/assetsComplete.png");
        TextureComponent textureComponent = base.getComponent(TextureComponent.class);
        base.addComponent(SpriteComponent.class, textureComponent);
        base.addComponent(TransformComponent.class,
                new Vector3f(0, 0, 0),
                new Vector3f(0.5f, 0, 0),
                new Vector3f(1, 1, 1));

        SpriteRendererSystem renderer = (SpriteRendererSystem) manager.getSystem(SpriteRendererSystem.class);
        renderer.addEntity(base);
    }

    @Override
    public void update(double deltaTime) {
    }

}
