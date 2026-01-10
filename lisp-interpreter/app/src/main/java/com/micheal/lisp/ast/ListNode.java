package com.micheal.lisp.ast;

import com.micheal.lisp.visitor.Visitor;

import java.util.List;

public class ListNode implements Node {

    private final List<Node> elements;

    public ListNode(List<Node> elements) {
        this.elements = elements;
    }

    public List<Node> getElements() {
        return elements;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
