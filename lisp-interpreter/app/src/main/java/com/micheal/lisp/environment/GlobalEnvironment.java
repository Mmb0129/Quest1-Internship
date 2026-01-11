package com.micheal.lisp.environment;

import java.util.HashMap;
import java.util.Map;

public class GlobalEnvironment {

    private static final GlobalEnvironment INSTANCE = new GlobalEnvironment();

    private final Map<String, Object> symbols = new HashMap<>();

    private GlobalEnvironment() {
    }

    public static GlobalEnvironment getInstance() {
        return INSTANCE;
    }

    public void define(String name, Object value) {
        symbols.put(name, value);
    }

    public Object lookup(String name) {
        if (!symbols.containsKey(name)) {
            throw new RuntimeException("Undefined symbol: " + name);
        }
        return symbols.get(name);
    }
}
