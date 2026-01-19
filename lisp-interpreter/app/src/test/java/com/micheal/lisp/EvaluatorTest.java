package com.micheal.lisp;

import com.micheal.lisp.parser.LispParser;
import com.micheal.lisp.visitor.EvaluationVisitor;
import com.micheal.lisp.ast.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class EvaluatorTest {
    
    private LispParser parser;
    private EvaluationVisitor evaluator;

    @Before
    public void setUp() {
        parser = new LispParser();
        evaluator = new EvaluationVisitor();
    }
    

    // Non-parameterized tests
    
    @Test
    public void testEvaluateNestedExpression() {
        Node ast = parser.parse("(+ 1 (* 2 3))");
        Object result = ast.accept(evaluator);
        assertEquals(7, result);
    }
    
    @Test
    public void testEvaluateComplexNestedExpression() {
        Node ast = parser.parse("(+ (* 2 3) (* 4 5))");
        Object result = ast.accept(evaluator);
        assertEquals(26, result);
    }
    
    @Test
    public void testDefineVariable() {
        Node ast = parser.parse("(define x 10)");
        Object result = ast.accept(evaluator);
        assertEquals(10, result);
        
        // verify variable was stored
        Node lookup = parser.parse("x");
        Object value = lookup.accept(evaluator);
        assertEquals(10, value);
    }
    
    @Test
    public void testUseVariable() {
        parser.parse("(define x 10)").accept(evaluator);
        parser.parse("(define y 20)").accept(evaluator);
        
        Node ast = parser.parse("(+ x y)");
        Object result = ast.accept(evaluator);
        assertEquals(30, result);
    }
    
    @Test
    public void testUseVariableInExpression() {
        parser.parse("(define a 5)").accept(evaluator);
        parser.parse("(define b 3)").accept(evaluator);
        
        Node ast = parser.parse("(* a b)");
        Object result = ast.accept(evaluator);
        assertEquals(15, result);
    }
    
    @Test
    public void testConditionalWithComparison() {
        Node ast = parser.parse("(if (> 10 5) 100 200)");
        Object result = ast.accept(evaluator);
        assertEquals(100, result);
    }
    
    @Test
    public void testConditionalFalse() {
        Node ast = parser.parse("(if (< 10 5) 100 200)");
        Object result = ast.accept(evaluator);
        assertEquals(200, result);
    }
}