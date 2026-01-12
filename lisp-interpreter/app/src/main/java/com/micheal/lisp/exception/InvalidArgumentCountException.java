package com.micheal.lisp.exception;

// when a function/operator is called with incorrect number of arguments, this exception is thrown

public class InvalidArgumentCountException extends EvaluationException {
    
    private final String operator;
    private final int expected;
    private final int actual;
    
    public InvalidArgumentCountException(String operator, int expected, int actual) {
        super(String.format("Operator '%s' expects %d argument(s), but got %d", 
            operator, expected, actual));
        this.operator = operator;
        this.expected = expected;
        this.actual = actual;
    }
    
    public InvalidArgumentCountException(String operator, String requirement) {
        super(String.format("Operator '%s' %s", operator, requirement));
        this.operator = operator;
        this.expected = -1;
        this.actual = -1;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public int getExpected() {
        return expected;
    }
    
    public int getActual() {
        return actual;
    }
}

