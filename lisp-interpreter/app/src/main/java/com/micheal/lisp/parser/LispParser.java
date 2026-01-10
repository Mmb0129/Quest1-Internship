package com.micheal.lisp.parser;

import com.micheal.lisp.ast.*;
import com.micheal.lisp.factory.NodeFactory;

import java.util.ArrayList;
import java.util.List;

public class LispParser {

    private final NodeFactory nodeFactory = new NodeFactory();

    public Node parse(String input) {
        input = input.trim();

        if (!input.startsWith("(") || !input.endsWith(")")) {
            throw new IllegalArgumentException("Invalid expression");
        }

        input = input.substring(1, input.length() - 1);
        String[] tokens = input.split("\\s+");

        List<Node> elements = new ArrayList<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                elements.add(nodeFactory.createNumber(Integer.parseInt(token)));
            } else {
                elements.add(nodeFactory.createSymbol(token));
            }
        }

        return nodeFactory.createList(elements);
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }
}
