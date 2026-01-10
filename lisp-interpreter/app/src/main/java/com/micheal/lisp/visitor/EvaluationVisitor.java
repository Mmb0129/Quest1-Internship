package com.micheal.lisp.visitor;

import com.micheal.lisp.ast.*;

public class EvaluationVisitor implements Visitor {

    @Override
    public Object visit(NumberNode node) {
        return null;
    }

    @Override
    public Object visit(SymbolNode node) {
        return null;
    }

    @Override
    public Object visit(ListNode node) {
        return null;
    }
}
