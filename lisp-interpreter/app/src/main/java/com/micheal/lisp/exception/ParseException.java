package com.micheal.lisp.exception;

// when a parsing error occurs, this exception is thrown

public class ParseException extends LispException {
    
    public ParseException(String message) {
        super(message);
    }
    
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

