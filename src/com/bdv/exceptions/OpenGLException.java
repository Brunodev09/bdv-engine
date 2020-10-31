package com.bdv.exceptions;

public class OpenGLException extends Exception {
    public OpenGLException(String msg) {
        super("[BDV]-[OPENGL]-[ERROR]-" + msg);
    }
}
