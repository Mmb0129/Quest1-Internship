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


}