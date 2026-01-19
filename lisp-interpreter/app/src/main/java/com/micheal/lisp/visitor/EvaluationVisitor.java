package com.micheal.lisp.visitor;

import com.micheal.lisp.ast.*;
import com.micheal.lisp.environment.GlobalEnvironment;
import com.micheal.lisp.exception.LispException;

import java.util.List;

public class EvaluationVisitor implements Visitor {

    @Override
    public Object visit(NumberNode node) {
        return node.getValue();
    }

    @Override
    public Object visit(SymbolNode node) {
        return GlobalEnvironment.getInstance().lookup(node.getName());
    }
    
    // helper to convert values to int for math ops
    private int toInt(Object value, String operation) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            throw new LispException(
                String.format("Arithmetic operation '%s' requires integer operands, got: %s", 
                    operation, value.getClass().getSimpleName()));
        }
        throw new LispException(
            String.format("Arithmetic operation '%s' requires numeric operands, got: %s", 
                operation, value.getClass().getSimpleName()));
    }


    @Override
    public Object visit(ListNode node) {
        var elements = node.getElements();

        if (elements.isEmpty()) {
            throw new LispException("Cannot evaluate empty list");
        }
        
        var first = elements.get(0);
        if (!(first instanceof SymbolNode)) {
            throw new LispException("First element must be an operator");
        }

        String op = ((SymbolNode) first).getName();

        switch (op) {
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
                throw new LispException("Unknown or invalid operator: '" + op + "'");
        }
    }

    private int evaluateAddition(List<Node> operands) {
        int result = 0;
        for (Node operand : operands) {
            Object value = operand.accept(this);
            result += toInt(value, "+");
        }
        return result;
    }

    private int evaluateSubtraction(List<Node> operands) {
        if (operands.isEmpty()) {
            throw new LispException("Operator '-' expects at least one argument");
        }

        int result = toInt(operands.get(0).accept(this), "-");
        
        // handle unary minus
        if (operands.size() == 1) {
            return -result;
        }

        for (int i = 1; i < operands.size(); i++) {
            result -= toInt(operands.get(i).accept(this), "-");
        }
        return result;
    }

    private int evaluateMultiplication(List<Node> operands) {
        int result = 1;
        for (Node operand : operands) {
            Object value = operand.accept(this);
            result *= toInt(value, "*");
        }
        return result;
    }

    private int evaluateDivision(List<Node> operands) {
        if (operands.isEmpty()) {
            throw new LispException("Operator '/' expects at least one argument");
        }

        int result = toInt(operands.get(0).accept(this), "/");

        for (int i = 1; i < operands.size(); i++) {
            int divisor = toInt(operands.get(i).accept(this), "/");
            if (divisor == 0) {
                throw new LispException("Arithmetic error in division: Division by zero");
            }
            result /= divisor;
        }

        return result;
    }

    private int evaluateModulo(List<Node> operands) {
        if (operands.size() != 2) {
            throw new LispException(String.format("Operator '%%' expects 2 argument(s), but got %d", operands.size()));
        }

        int a = toInt(operands.get(0).accept(this), "%");
        int b = toInt(operands.get(1).accept(this), "%");

        if (b == 0) {
            throw new LispException("Arithmetic error in modulo: Modulo by zero");
        }

        return a % b;
    }


    private Object evaluateDefine(List<Node> elements) {
        if (elements.size() != 3) {
            throw new LispException(String.format("Operator 'define' expects 2 argument(s), but got %d", elements.size() - 1));
        }

        Node nameNode = elements.get(1);
        if (!(nameNode instanceof SymbolNode)) {
            throw new LispException("First argument to 'define' must be a symbol");
        }

        String name = ((SymbolNode) nameNode).getName();
        Object value = elements.get(2).accept(this);
        GlobalEnvironment.getInstance().define(name, value);
        return value;
    }

    private Object evaluateGreaterThan(List<Node> elements) {
        if (elements.size() != 3) {
            throw new LispException(String.format("Operator '>' expects 2 argument(s), but got %d", elements.size() - 1));
        }

        int left = toInt(elements.get(1).accept(this), ">");
        int right = toInt(elements.get(2).accept(this), ">");

        return left > right;
    }

    private Object evaluateLessThan(List<Node> elements) {
        if (elements.size() != 3) {
            throw new LispException(String.format("Operator '<' expects 2 argument(s), but got %d", elements.size() - 1));
        }

        int left = toInt(elements.get(1).accept(this), "<");
        int right = toInt(elements.get(2).accept(this), "<");

        return left < right;
    }

    private Object evaluateEquals(List<Node> elements) {
        if (elements.size() != 3) {
            throw new LispException(String.format("Operator '=' expects 2 argument(s), but got %d", elements.size() - 1));
        }

        Object left = elements.get(1).accept(this);
        Object right = elements.get(2).accept(this);

        return left.equals(right);
    }

    private Object evaluateIf(List<Node> elements) {
        if (elements.size() != 4) {
            throw new LispException(String.format("Operator 'if' expects 3 argument(s), but got %d", elements.size() - 1));
        }

        Object condition = elements.get(1).accept(this);
        if (!(condition instanceof Boolean)) {
            throw new LispException("'if' condition must evaluate to a boolean value, got: " + 
                condition.getClass().getSimpleName());
        }

        if ((Boolean) condition) {
            return elements.get(2).accept(this);
        }
        return elements.get(3).accept(this);
    }
}
