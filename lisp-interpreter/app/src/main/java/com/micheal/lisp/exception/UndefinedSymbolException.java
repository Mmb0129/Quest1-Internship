package com.micheal.lisp.exception;

// when a symbol is referenced that is not defined, this exception is thrown

public class UndefinedSymbolException extends EvaluationException {
    
    private final String symbolName;
    
    public UndefinedSymbolException(String symbolName) {
        super("Undefined symbol: '" + symbolName + "'");
        this.symbolName = symbolName;
    }
    
    public String getSymbolName() {
        return symbolName;
    }
}

