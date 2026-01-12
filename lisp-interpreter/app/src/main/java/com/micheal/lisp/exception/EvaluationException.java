package com.micheal.lisp.exception;

// when an error occurs during expression evaluation, this exception is thrown

public class EvaluationException extends LispException {
    
    public EvaluationException(String message) {
        super(message);
    }
    
    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}

