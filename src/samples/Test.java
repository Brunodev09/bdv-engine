package samples;

import com.bdv.ECS.Entity;
import com.bdv.Main;
import com.bdv.api.BdvScript;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TextureComponent;

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
            InvocationTargetException {

        Entity base = manager.createEntity();
        TextureComponent.bindAssetsStore(assetPool);
        base.addComponent(TextureComponent.class, "full_spritesheet", "images/assetsComplete.png");
        TextureComponent textureComponent = base.<TextureComponent>getComponent();
        base.addComponent(SpriteComponent.class, textureComponent);
    }

    @Override
    public void update(double deltaTime) {
    }

}
