package samples;

import com.bdv.ECS.Entity;
import com.bdv.Main;
import com.bdv.api.BdvScript;
import com.bdv.systems.RenderSystem;

public class Test extends BdvScript {
    public static void main(String[] args) {
        try {
            new Main(Test.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        Entity base = manager.createEntity();
    }

    @Override
    public void update(double deltaTime) {
    }

}
