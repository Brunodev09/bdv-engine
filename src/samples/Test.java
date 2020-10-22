package samples;

import com.bdv.Main;
import com.bdv.api.BdvScript;

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

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render() {

    }
}
