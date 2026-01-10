package com.micheal.lisp.visitor;

import com.micheal.lisp.ast.*;

import java.util.List;

public class EvaluationVisitor implements Visitor {


    @Override
    public Object visit(NumberNode node) {
        return node.getValue();
    }


    @Override
    public Object visit(SymbolNode node) {
        return null;
    }

    @Override
    public Object visit(ListNode node) {
        var elements= node.getElements();

        if(elements.isEmpty()){
            throw new IllegalStateException("Cannot evaluate empty list");
        }
        var operatorNode= elements.get(0);

        if(!(operatorNode instanceof SymbolNode)){
            throw new IllegalArgumentException("First element must be an operator");

        }

        String operator = ((SymbolNode) operatorNode).getName();

        switch (operator) {
            case "+":
                return evaluateAddition(elements.subList(1, elements.size()));
            case "*":
                return evaluateMultiplication(elements.subList(1, elements.size()));
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator);
        }

    }

    private int evaluateAddition(List<Node> operands) {
        int result = 0;
        for (Node operand : operands) {
            Object value = operand.accept(this);
            result += (int) value;
        }
        return result;
    }

    private int evaluateMultiplication(List<Node> operands) {
        int result = 1;
        for (Node operand : operands) {
            Object value = operand.accept(this);
            result *= (int) value;
        }
        return result;
    }


}
