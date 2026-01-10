package com.micheal.lisp.ast;

import com.micheal.lisp.visitor.Visitor;

public class SymbolNode implements Node {

    private final String name;

    public SymbolNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
