package samples;

import com.bdv.ECS.Entity;
import com.bdv.Main;
import com.bdv.api.BdvScript;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TextureComponent;
import com.bdv.components.TransformComponent;
import com.bdv.exceptions.InvalidInstance;
import com.bdv.systems.SpriteRendererSystem;
import org.lwjgl.util.Dimension;
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

        base.addComponent(TextureComponent.class, "green", "images/green.png");
        TextureComponent textureComponent = base.getComponent(TextureComponent.class);
        base.addComponent(SpriteComponent.class, textureComponent);
        base.addComponent(TransformComponent.class,
                new Vector3f(0, 100, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1),
                new Dimension(100, 100));

        Entity additional = manager.createEntity();

        additional.addComponent(TextureComponent.class, "grey", "images/grey.png");
        TextureComponent textureComponent2 = additional.getComponent(TextureComponent.class);
        additional.addComponent(SpriteComponent.class, textureComponent2);
        additional.addComponent(TransformComponent.class,
                new Vector3f(200, 0, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1),
                new Dimension(100, 100));

        SpriteRendererSystem renderer = (SpriteRendererSystem) manager.getSystem(SpriteRendererSystem.class);
        renderer.addEntity(base);
        renderer.addEntity(additional);
    }

    @Override
    public void update(double deltaTime) {
    }

}
