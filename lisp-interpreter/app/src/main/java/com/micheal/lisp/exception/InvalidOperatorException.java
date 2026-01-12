package com.micheal.lisp.exception;

// when an invalid or unsupported operator is used, this exception is thrown

public class InvalidOperatorException extends EvaluationException {
    
    private final String operator;
    
    public InvalidOperatorException(String operator) {
        super("Unknown or invalid operator: '" + operator + "'");
        this.operator = operator;
    }
    
    public InvalidOperatorException(String operator, String reason) {
        super("Invalid operator '" + operator + "': " + reason);
        this.operator = operator;
    }
    
    public String getOperator() {
        return operator;
    }
}

