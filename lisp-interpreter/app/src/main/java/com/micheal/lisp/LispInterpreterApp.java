package com.micheal.lisp;

import com.micheal.lisp.repl.Repl;

public class LispInterpreterApp {

    public static void main(String[] args) {
        Repl repl = new Repl();
        repl.start();
    }
}
