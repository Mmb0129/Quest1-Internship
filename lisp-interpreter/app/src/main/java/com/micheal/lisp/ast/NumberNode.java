package com.micheal.lisp.ast;

import com.micheal.lisp.visitor.Visitor;

public class NumberNode implements Node {

    private final int value;

    public NumberNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
