package examples.dungeon.generation;

import java.util.logging.Logger;

public class EmptyField extends Location {
    private static final Logger LOG = Logger.getLogger(EmptyField.class.getName());

    public EmptyField(int x, int y, int z, int width, int height, Class<?> tileClass) {
        super(x, y, z, width, height, tileClass);
    }

    public void generate() {

    }
}
