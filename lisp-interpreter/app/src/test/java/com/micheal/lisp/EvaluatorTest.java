package com.micheal.lisp;

import com.micheal.lisp.environment.GlobalEnvironment;
import com.micheal.lisp.parser.LispParser;
import com.micheal.lisp.visitor.EvaluationVisitor;
import com.micheal.lisp.exception.*;
import org.junit.Test;
import org.junit.Before;
import com.micheal.lisp.ast.*;
import static org.junit.Assert.*;


public class EvaluatorTest {
    
    private LispParser parser;
    private EvaluationVisitor evaluator;
    
    @Before
    public void setUp() {
        parser = new LispParser();
        evaluator = new EvaluationVisitor();
    }
   

    @Test
    public void testEvaluateNumber() {
        Node ast = parser.parse("32");
        Object result = ast.accept(evaluator);
        assertEquals(32, result);
    }

    @Test
    public void testEvaluateAddition() {
        Node ast = parser.parse("(+ 1 2)");
        Object result = ast.accept(evaluator);
        assertEquals(3, result);
    }

    @Test
    public void testEvaluateMultipleAddition() {
        Node ast = parser.parse("(+ 1 2 3 4)");
        Object result = ast.accept(evaluator);
        assertEquals(10, result);
    }
    
    @Test
    public void testEvaluateSubtraction() {
        Node ast = parser.parse("(- 10 3)");
        Object result = ast.accept(evaluator);
        assertEquals(7, result);
    }
    
    @Test
    public void testEvaluateUnaryMinus() {
        Node ast = parser.parse("(- 5)");
        Object result = ast.accept(evaluator);
        assertEquals(-5, result);
    }
    
    @Test
    public void testEvaluateMultiplication() {
        Node ast = parser.parse("(* 2 3 4)");
        Object result = ast.accept(evaluator);
        assertEquals(24, result);
    }
    
    @Test
    public void testEvaluateDivision() {
        Node ast = parser.parse("(/ 20 4)");
        Object result = ast.accept(evaluator);
        assertEquals(5, result);
    }
    
    @Test
    public void testEvaluateModulo() {
        Node ast = parser.parse("(% 10 3)");
        Object result = ast.accept(evaluator);
        assertEquals(1, result);
    }
    
    @Test
    public void testEvaluateNestedExpression() {
        Node ast = parser.parse("(+ 1 (* 2 3))");
        Object result = ast.accept(evaluator);
        assertEquals(7, result);
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
    public void testGreaterThan() {
        Node ast = parser.parse("(> 10 5)");
        Object result = ast.accept(evaluator);
        assertEquals(true, result);
    }
    
    @Test
    public void testLessThan() {
        Node ast = parser.parse("(< 3 7)");
        Object result = ast.accept(evaluator);
        assertEquals(true, result);
    }
    
    @Test
    public void testEquals() {
        Node ast = parser.parse("(= 5 5)");
        Object result = ast.accept(evaluator);
        assertEquals(true, result);
    }

}