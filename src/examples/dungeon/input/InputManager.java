package examples.dungeon.input;

public class InputManager {
    private final Keyboard keyboard;
    private final Mouse mouse;
    private static InputManager instantiated;

    public static InputManager newInstance(Keyboard keyboard, Mouse mouse) {
        if (instantiated == null) {
            instantiated = new InputManager(keyboard, mouse);
        }
        return instantiated;
    }

    private InputManager(Keyboard keyboard, Mouse mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public static Keyboard getKeyboard() {
        return instantiated.keyboard;
    }

    public static Mouse getMouse() {
        return instantiated.mouse;
    }

    public static InputManager getInstance() {
        return instantiated;
    }
}
