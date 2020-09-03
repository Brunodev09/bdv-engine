package engine.api;

import engine.video.RenderManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class InputAPI {
    private InputAPI() {
    }

    public static List<String> listenForKeyboard() {
        final List<String> keys = new ArrayList<>();
        long windowContext = RenderManager.getWindow();

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
}
