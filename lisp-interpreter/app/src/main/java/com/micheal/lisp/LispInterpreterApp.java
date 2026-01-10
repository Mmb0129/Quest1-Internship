package com.micheal.lisp;
import com.micheal.lisp.ast.*;
import com.micheal.lisp.visitor.EvaluationVisitor;

import java.util.List;

public class LispInterpreterApp {

    public static void main(String[] args) {
        EvaluationVisitor evaluator = new EvaluationVisitor();

        Node expression = new ListNode(
                List.of(
                        new SymbolNode("+"),
                        new NumberNode(1),
                        new NumberNode(2)
                )
        );

        Object result = expression.accept(evaluator);
        System.out.println(result); // should print 3
    }
}
