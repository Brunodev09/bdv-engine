#bdv-engine-java

## Table of contents
* [Information](#general-info)
* [Samples](#samples)
* [Technologies](#technologies)
* [Demo](#setup)

## General info
This is a project where I migrate most of the graphical stuff that I've been working on with Javascript and CANVAS to an actual attempt to create a game engine layer that implements the OpenGL 3.1 API (2D and 3D) bindings through LWJGL. In such, the user would not be required to actually write shaders but instead interact with my API to actually load data into the rendering pipeline. While this is surely slower than actually buffering the GPU directly, it surely is easier to prototype. It also contains an optional rendering option through Java Swing.

## Samples
Samples I made with this engine includes:

- Terrain generation (2D and 3D)
- Perlin noise
- Mandelbrot sets
- Dijkstra and A* pathfinding implementations
- I still intend to implment ray-casting, 2D lighting and raycasting, 2D ray marching, fluid simulation and a game maybe? :D

![Basic 3D rendering with specular lighting](./samples/basic.gif)

![Conways Game of Life](./samples/conways.gif)
![Conways Game of Life](./samples/conways2.gif)

![astar](./samples/astar.gif)

![mandelbrot](./samples/mandelbrot.gif)

![perlin](./samples/perlin.PNG)
![perlin2](./samples/perlin2.gif)



	
## Technologies
Project is created with:
* Java (>=1.8)
* lwjgl-2.9.3
	
## Setup
There is currently no installation setup, you can download it  and create your own scripts inside  ``./src/app/Templates`` and there you can choose either of the rendering options: native or OpenGL. And you do that by extending either the abstract class `Script` or `ScriptGL`. Below there is a quick example of a simple texture grid (2D) being loaded into the screen using both approaches. Textures and OBJ files must be placed inside `./src/res` folder. Entrypoint is in `./src/app/App.java`

## OpenGL rendering API
```
package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GL_TEST_GRID extends ScriptGL {

    public GL_TEST_GRID() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0,0,0,255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        // In OpenGL, the default viewport sets the origin (0,0,0) at the center of the screen
        int rows = 12;
        int cols = 12;
        Dimension tileSize = new Dimension(this.resolution.width / rows, this.resolution.height / cols);
        for (int i = -rows / 2; i < rows / 2; i++) {
            for (int j = -cols / 2; j < cols / 2; j++) {
                this.entities.add(new EntityAPI("grass2", new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0)));
            }
        }
    }

    @Override
    public void update() {
    }
}

```

## Java Swing rendering API

```
package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GL_TEST_GRID extends ScriptGL {

    public GL_TEST_GRID() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0,0,0,255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        // In OpenGL, the default viewport sets the origin (0,0,0) at the center of the screen
        int rows = 12;
        int cols = 12;
        Dimension tileSize = new Dimension(this.resolution.width / rows, this.resolution.height / cols);
        for (int i = -rows / 2; i < rows / 2; i++) {
            for (int j = -cols / 2; j < cols / 2; j++) {
                this.entities.add(new EntityAPI("grass2", new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0)));
            }
        }
    }

    @Override
    public void update() {
    }
}

```