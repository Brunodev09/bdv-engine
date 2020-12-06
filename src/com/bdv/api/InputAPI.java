package com.bdv.api;

import com.bdv.api.events.BdvKeyEvent;
import com.bdv.api.events.BdvMouseEvent;
import com.bdv.renders.opengl.OpenGLRenderManager;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;

public class InputAPI {
    private static Logger LOG = Logger.getLogger(InputAPI.class.getName());

    private BdvMouseEvent mouseCallback;
    private BdvKeyEvent keyCallback;

    private GLFWCursorPosCallback posCallback;
    private GLFWMouseButtonCallback glfwMouseCallback;
    private GLFWKeyCallback glfwKeyCallback;

    public InputAPI() {
    }

    public void registerMouseCallback(BdvMouseEvent callback) {
        this.mouseCallback = callback;
    }

    public void registerKeyCallback(BdvKeyEvent callback) {
        this.keyCallback = callback;
    }

    public void setupInputListener() {
        long windowContext = OpenGLRenderManager.getWindow();

        BdvMouseEvent mouseCb = this.mouseCallback;
        BdvKeyEvent keyCb = this.keyCallback;

        glfwSetCursorPosCallback(windowContext, posCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                if (mouseCb != null) {
                    mouseCb.onMove(xpos, ypos);
                }
            }
        });
        glfwSetMouseButtonCallback(windowContext, glfwMouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (mouseCb != null) {
                    if (button == GLFW_MOUSE_BUTTON_1) {
                        mouseCb.onLeftClick(true);
                    } else if (button == GLFW_MOUSE_BUTTON_2) {
                        mouseCb.onRightClick(true);
                    }
                }
            }
        });

        glfwSetKeyCallback(windowContext, glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (keyCb != null) {
                    String _action = null;
                    String _key = null;
                    switch (action) {
                        case GLFW_PRESS:
                            _action = "PRESS";
                            break;
                        case GLFW_RELEASE:
                            _action = "RELEASE";
                            break;
                    }
                    switch (key) {
                        case GLFW_KEY_ESCAPE:
                            _key = "ESCAPE";
                            break;
                        case GLFW_KEY_ENTER:
                            _key = "ENTER";
                            break;
                        case GLFW_KEY_TAB:
                            _key = "TAB";
                            break;
                        case GLFW_KEY_BACKSPACE:
                            _key = "BACKSPACE";
                            break;
                        case GLFW_KEY_RIGHT:
                            _key = "ARROW_RIGHT";
                            break;
                        case GLFW_KEY_LEFT:
                            _key = "ARROW_LEFT";
                            break;
                        case GLFW_KEY_DOWN:
                            _key = "ARROW_DOWN";
                            break;
                        case GLFW_KEY_UP:
                            _key = "ARROW_UP";
                            break;
                        case GLFW_KEY_A:
                            _key = "A";
                            break;
                        case GLFW_KEY_B:
                            _key = "B";
                            break;
                        case GLFW_KEY_C:
                            _key = "C";
                            break;
                        case GLFW_KEY_D:
                            _key = "D";
                            break;
                        case GLFW_KEY_E:
                            _key = "E";
                            break;
                        case GLFW_KEY_F:
                            _key = "F";
                            break;
                        case GLFW_KEY_G:
                            _key = "G";
                            break;
                        case GLFW_KEY_H:
                            _key = "H";
                            break;
                        case GLFW_KEY_I:
                            _key = "I";
                            break;
                        case GLFW_KEY_J:
                            _key = "J";
                            break;
                        case GLFW_KEY_K:
                            _key = "K";
                            break;
                        case GLFW_KEY_L:
                            _key = "L";
                            break;
                        case GLFW_KEY_M:
                            _key = "M";
                            break;
                        case GLFW_KEY_N:
                            _key = "N";
                            break;
                        case GLFW_KEY_O:
                            _key = "O";
                            break;
                        case GLFW_KEY_P:
                            _key = "P";
                            break;
                        case GLFW_KEY_Q:
                            _key = "Q";
                            break;
                        case GLFW_KEY_R:
                            _key = "R";
                            break;
                        case GLFW_KEY_S:
                            _key = "S";
                            break;
                        case GLFW_KEY_T:
                            _key = "T";
                            break;
                        case GLFW_KEY_U:
                            _key = "U";
                            break;
                        case GLFW_KEY_V:
                            _key = "V";
                            break;
                        case GLFW_KEY_W:
                            _key = "W";
                            break;
                        case GLFW_KEY_X:
                            _key = "X";
                            break;
                        case GLFW_KEY_Y:
                            _key = "Y";
                            break;
                        case GLFW_KEY_Z:
                            _key = "Z";
                            break;

                    }
                    if (_action != null && _key != null)
                        keyCb.onKeyEvent(_key, _action);
                }
            }
        });
    }

    public static List<String> listenForKeyboard() {
        final List<String> keys = new ArrayList<>();
        long windowContext = OpenGLRenderManager.getWindow();

        if (glfwGetKey(windowContext, GLFW_KEY_W) == GLFW_PRESS) {
            keys.add("W");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_S) == GLFW_PRESS) {
            keys.add("S");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_D) == GLFW_PRESS) {
            keys.add("D");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_A) == GLFW_PRESS) {
            keys.add("A");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_Q) == GLFW_PRESS) {
            keys.add("Q");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_E) == GLFW_PRESS) {
            keys.add("E");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_R) == GLFW_PRESS) {
            keys.add("R");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_T) == GLFW_PRESS) {
            keys.add("T");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_Y) == GLFW_PRESS) {
            keys.add("Y");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_U) == GLFW_PRESS) {
            keys.add("U");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_ENTER) == GLFW_PRESS) {
            keys.add("ENTER");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_BACKSPACE) == GLFW_PRESS) {
            keys.add("BACKSPACE");
        }
        if (glfwGetKey(windowContext, GLFW_KEY_TAB) == GLFW_PRESS) {
            keys.add("TAB");
        }
        return keys;
    }

    public void destroy() {
        this.glfwKeyCallback.free();
        this.glfwMouseCallback.free();
    }
}
