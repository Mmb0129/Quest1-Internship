package com.micheal.lisp.repl;

import com.micheal.lisp.parser.LispParser;
import com.micheal.lisp.visitor.EvaluationVisitor;
import com.micheal.lisp.ast.Node;

import java.util.Scanner;

public class Repl {

    private final LispParser parser;
    private final EvaluationVisitor evaluator;

    public Repl() {
        this.parser = new LispParser();
        this.evaluator = new EvaluationVisitor();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Lisp Interpreter");
        System.out.println("Type 'exit' to quit");

        while (true) {
            System.out.print("lisp> ");

            if (!scanner.hasNextLine()) {
                System.out.println("\nInput stream closed. Exiting...");
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            try {
                Node ast = parser.parse(input);
                Object result = ast.accept(evaluator);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }
}
