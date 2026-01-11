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

    // ------------------ Parsing ------------------

    private Node parseExpression() {
        String token = tokens.get(position);

        if ("(".equals(token)) {
            position++; // consume '('
            List<Node> elements = new ArrayList<>();

            while (!")".equals(tokens.get(position))) {
                elements.add(parseExpression());
            }

            position++; // consume ')'
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

    // ------------------ Tokenizer ------------------

    private List<String> tokenize(String input) {
        input = input.replace("(", " ( ").replace(")", " ) ");
        String[] rawTokens = input.trim().split("\\s+");

        List<String> result = new ArrayList<>();
        for (String token : rawTokens) {
            if (!token.isBlank()) {
                result.add(token);
            }
        }
        return result;
    }

    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }
}
