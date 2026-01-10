package com.micheal.lisp;
import com.micheal.lisp.ast.*;
import com.micheal.lisp.repl.Repl;
import com.micheal.lisp.visitor.EvaluationVisitor;

import java.util.List;

public class LispInterpreterApp {

    public static void main(String[] args) {
        Repl repl = new Repl();
        repl.start();
    }
}
