package com.micheal.lisp.parser;

import com.micheal.lisp.ast.Node;
import com.micheal.lisp.factory.NodeFactory;

import java.util.ArrayList;
import java.util.List;

public class LispParser {

    private final NodeFactory nodeFactory = new NodeFactory();
    private List<String> tokens;
    private int position;

    public Node parse(String input) {
        this.tokens = tokenize(input);
        this.position = 0;
        return parseExpression();
    }

    private Node parseExpression() {
        String token = tokens.get(position);

        if ("(".equals(token)) {
            position++; // skip '('
            List<Node> elements = new ArrayList<>();

            while (!")".equals(tokens.get(position))) {
                elements.add(parseExpression());
            }
            position++; // skip ')'
            return nodeFactory.createList(elements);
        }

        position++;
        return parseAtom(token);
    }

    private Node parseAtom(String token) {
        if (isNumber(token)) {
            return nodeFactory.createNumber(Integer.parseInt(token));
        }
        return nodeFactory.createSymbol(token);
    }

    private List<String> tokenize(String input) {
        // add spaces around parens so they become separate tokens
        input = input.replace("(", " ( ").replace(")", " ) ");
        String[] parts = input.trim().split("\\s+");

        List<String> tokens = new ArrayList<>();
        for (String part : parts) {
            if (!part.isBlank()) {
                tokens.add(part);
            }
        }
        return tokens;
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }
}
