package com.micheal.lisp.ast;

import com.micheal.lisp.visitor.Visitor;

public interface Node {
    Object accept(Visitor visitor);
}
