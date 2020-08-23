package engine.video;

import engine.core.interfaces.Entity;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Render {

    private RenderQueue _queue;
    private BufferedImage image;
    private int _width;
    private int _height;
    private int[] pixels;
    private int background;

    public Render() {
    }

    public Render(RenderQueue queue, int width, int height) {
        _width = width;
        _height = height;
        _queue = queue;
    }

    public void init() {
        image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void setDimensions(int w, int h) {
        this._width = w;
        this._height = h;
    }

    public void replaceQueue(RenderQueue queue) {
        _queue = queue;
    }

    public void setBackground(int color) {
        background = color;
    }

    public void render(BufferStrategy buffer) {

        Arrays.fill(pixels, this.background);
        Graphics display = buffer.getDrawGraphics();
        display.drawImage(image, 0, 0, _width, _height, null);

        if (this._queue.getRenderQueue().size() > 0) {
            for (Entity renderable : this._queue.getRenderQueue()) {
                switch (renderable.getMdl()) {
                    case TEXTURE:
                        break;
                    case SPRITESHEET:
                        break;
                    case ARC:
                        break;
                    case TEXT:
                        break;
                    case RECTANGLE:
                        int[] color = renderable.getColor().getColorCodes();
                        display.setColor(new Color(color[0], color[1], color[2], color[3]));
                        display.fillRect((int) renderable.getPosition().x, (int) renderable.getPosition().y,
                                renderable.getDimension().width, renderable.getDimension().height);
                        break;
                    case POINT:
                        int[] color2 = renderable.getColor().getColorCodes();
                        display.setColor(new Color(color2[0], color2[1], color2[2], color2[3]));

                        Graphics2D g2d = (Graphics2D) display;
                        int width = 20;
                        g2d.setStroke(new BasicStroke(width));

                        g2d.drawLine(
                                (int) renderable.getPosition().x,
                                (int) renderable.getPosition().y,
                                (int) renderable.getPosition().x,
                                (int) renderable.getPosition().y);
                        break;
                }
            }
        }

        display.dispose();
        buffer.show();
    }
}
