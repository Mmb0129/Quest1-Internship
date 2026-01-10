package com.micheal.lisp.visitor;

import com.micheal.lisp.ast.*;

public interface Visitor {

    Object visit(NumberNode node);

    Object visit(SymbolNode node);

    Object visit(ListNode node);
}
