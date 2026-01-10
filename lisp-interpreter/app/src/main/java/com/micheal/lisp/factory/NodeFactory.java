package com.micheal.lisp.factory;

import com.micheal.lisp.ast.*;

import java.util.List;

public class NodeFactory {

    public NumberNode createNumber(int value) {
        return new NumberNode(value);
    }

    public SymbolNode createSymbol(String name) {
        return new SymbolNode(name);
    }

    public ListNode createList(List<Node> elements) {
        return new ListNode(elements);
    }
}
