package com.micheal.lisp.environment;

import com.micheal.lisp.exception.LispException;

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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol name cannot be null or empty");
        }
        symbols.put(name, value);
    }

    public Object lookup(String name) {
        if (!symbols.containsKey(name)) {
            throw new LispException("Undefined symbol: '" + name + "'");
        }
        return symbols.get(name);
    }
}
