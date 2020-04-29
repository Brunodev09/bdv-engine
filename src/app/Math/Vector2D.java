package app.Math;

public class Vector2D {
    public Point<Float> Components;

    private int module;

    public Vector2D(Point<Float> components)
    {
        Components = new Point<Float>(components.x, components.y);
    }

    public void SumOrSubtract(Point<Float> components)
    {
        Components.x += components.x;
        Components.y += components.y;
    }

    public int getModule() {
        return module;
    }
}
