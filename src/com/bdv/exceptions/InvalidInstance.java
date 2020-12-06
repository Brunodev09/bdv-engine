package com.bdv.exceptions;

public class InvalidInstance extends Exception {
    public InvalidInstance(String extend) {
        super("[BDV]-[FATAL]-[ERROR]- Received script instance is not a valid instance. Please extend " + extend + ".");
    }
}
