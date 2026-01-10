package com.micheal.lisp.environment;

public class GlobalEnvironment {

    private static final GlobalEnvironment INSTANCE = new GlobalEnvironment();

    private GlobalEnvironment() {
    }

    public static GlobalEnvironment getInstance() {
        return INSTANCE;
    }
}
