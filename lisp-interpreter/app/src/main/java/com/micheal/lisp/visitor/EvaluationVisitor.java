package com.micheal.lisp.visitor;

import com.micheal.lisp.ast.*;
import com.micheal.lisp.environment.GlobalEnvironment;

import java.util.List;

public class EvaluationVisitor implements Visitor {


    @Override
    public Object visit(NumberNode node) {
        return node.getValue();
    }


    @Override
    public Object visit(SymbolNode node) {
        return GlobalEnvironment
                .getInstance()
                .lookup(node.getName());
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
            case "-":
                return evaluateSubtraction(elements.subList(1, elements.size()));
            case "*":
                return evaluateMultiplication(elements.subList(1, elements.size()));
            case "/":
                return evaluateDivision(elements.subList(1, elements.size()));
            case "%":
                return evaluateModulo(elements.subList(1, elements.size()));
            case "define":
                return evaluateDefine(elements);
            case "if":
                return evaluateIf(elements);
            case ">":
                return evaluateGreaterThan(elements);
            case "<":
                return evaluateLessThan(elements);
            case "=":
                return evaluateEquals(elements);
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

    private int evaluateSubtraction(List<Node> operands) {
        if (operands.isEmpty()) {
            throw new RuntimeException("- expects at least one argument");
        }

        int result = (int) operands.get(0).accept(this);

        if (operands.size() == 1) {
            return -result; // unary minus
        }

        for (int i = 1; i < operands.size(); i++) {
            result -= (int) operands.get(i).accept(this);
        }

        return result;
    }

    private int evaluateMultiplication (List<Node> operands) {
        int result = 1;
        for (Node operand : operands) {
            Object value = operand.accept(this);
            result *= (int) value;
        }
        return result;
    }

    private int evaluateDivision(List<Node> operands) {
        if (operands.isEmpty()) {
            throw new RuntimeException("/ expects at least one argument");
        }

        int result = (int) operands.get(0).accept(this);

        for (int i = 1; i < operands.size(); i++) {
            int divisor = (int) operands.get(i).accept(this);
            if (divisor == 0) {
                throw new ArithmeticException("Division by zero");
            }
            result /= divisor;
        }

        return result;
    }

    private int evaluateModulo(List<Node> operands) {
        if (operands.size() != 2) {
            throw new RuntimeException("% expects exactly 2 arguments");
        }

        int left = (int) operands.get(0).accept(this);
        int right = (int) operands.get(1).accept(this);

        if (right == 0) {
            throw new ArithmeticException("Modulo by zero");
        }

        return left % right;
    }


    private Object evaluateDefine(List<Node> elements) {
        if (elements.size() != 3) {
            throw new RuntimeException("define expects 2 arguments");
        }

        if (!(elements.get(1) instanceof SymbolNode)) {
            throw new RuntimeException("First argument to define must be a symbol");
        }

        String name = ((SymbolNode) elements.get(1)).getName();
        Object value = elements.get(2).accept(this);

        GlobalEnvironment.getInstance().define(name, value);
        return value;
    }

    private Object evaluateGreaterThan(List<Node> elements) {
        if (elements.size() != 3) {
            throw new RuntimeException("> expects 2 arguments");
        }

        int left = (int) elements.get(1).accept(this);
        int right = (int) elements.get(2).accept(this);

        return left > right;
    }

    private Object evaluateLessThan(List<Node> elements) {
        if (elements.size() != 3) {
            throw new RuntimeException("< expects 2 arguments");
        }

        int left = (int) elements.get(1).accept(this);
        int right = (int) elements.get(2).accept(this);

        return left < right;
    }

    private Object evaluateEquals(List<Node> elements) {
        if (elements.size() != 3) {
            throw new RuntimeException("= expects 2 arguments");
        }

        Object left = elements.get(1).accept(this);
        Object right = elements.get(2).accept(this);

        return left.equals(right);
    }

    private Object evaluateIf(List<Node> elements) {
        if (elements.size() != 4) {
            throw new RuntimeException("if expects 3 arguments");
        }

        Object condition = elements.get(1).accept(this);

        if (!(condition instanceof Boolean)) {
            throw new RuntimeException("if condition must be boolean");
        }

        if ((Boolean) condition) {
            return elements.get(2).accept(this);
        } else {
            return elements.get(3).accept(this);
        }
    }




}
