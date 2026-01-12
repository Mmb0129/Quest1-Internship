package com.micheal.lisp.exception;

// base exception class for all my lisp exceptions

public class LispException extends RuntimeException {
    
    public LispException(String message) {
        super(message);
    }
    
    public LispException(String message, Throwable cause) {
        super(message, cause);
    }
}

