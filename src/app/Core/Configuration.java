package app.Core;

import app.API.ScriptGL;

public class Configuration {
    public final int WIDTH;
    public final int HEIGHT;
    public final int FPS;
    public final String TITLE;
    public final ScriptGL script;

    public Configuration(int WIDTH, int HEIGHT, int FPS, String TITLE, ScriptGL script) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.FPS = FPS;
        this.TITLE = TITLE;
        this.script = script;
    }

    public Configuration() {
        this.WIDTH = 800;
        this.HEIGHT = 600;
        this.FPS = 60;
        this.TITLE = "Unnamed video service";
        this.script = null;
    }

}
