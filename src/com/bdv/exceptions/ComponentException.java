package com.bdv.exceptions;


public class ComponentException extends Exception {
    public ComponentException(String message) {
        super("[BDV]-[ERROR]-[COMPONENT]- " + message);
    }
}
