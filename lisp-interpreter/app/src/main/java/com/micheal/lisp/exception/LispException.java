package com.micheal.lisp.exception;

// base exception class for all my lisp exceptions

public class LispException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LispException(String message) {
        super(message);
    }
    
    public LispException(String message, Throwable cause) {
        super(message, cause);
    }
}

