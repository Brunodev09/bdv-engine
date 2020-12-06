package com.bdv.exceptions;

public class OpenGLTextureProcessorException extends Exception {
    public OpenGLTextureProcessorException(String message) {
        super("[BDV]-[OPENGL_RENDERER]-[ERROR]-" + message);
    }
}
