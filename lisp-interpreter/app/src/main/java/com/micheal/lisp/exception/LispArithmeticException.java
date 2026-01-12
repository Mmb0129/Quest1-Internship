package com.micheal.lisp.exception;

// when an arithmetic error occurs (e.g., division by zero), this exception is thrown

public class LispArithmeticException extends EvaluationException {
    
    public LispArithmeticException(String message) {
        super(message);
    }
    
    public LispArithmeticException(String operation, String reason) {
        super(String.format("Arithmetic error in %s: %s", operation, reason));
    }
}

