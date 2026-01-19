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

@RunWith(Parameterized.class)
public class EvaluatorParameterizedTest {

    private LispParser parser;
    private EvaluationVisitor evaluator;

    // Parameters for parameterized tests
    private final String expression;
    private final Object expectedResult;

    @Before
    public void setUp() {
        parser = new LispParser();
        evaluator = new EvaluationVisitor();
    }

    // Constructor for parameterized tests
    public EvaluatorParameterizedTest(String expression, Object expectedResult) {
        this.expression = expression;
        this.expectedResult = expectedResult;
    }

    // Test data for parameterized tests
    @Parameters(name = "{0} = {1}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                // Number evaluation
                {"32", 32},
                {"-10", -10},
                {"0", 0},
                {"100", 100},

                // Addition
                {"(+ 1 2)", 3},
                {"(+ 5 5)", 10},
                {"(+ 1 2 3)", 6},
                {"(+ 1 2 3 4)", 10},
                {"(+ 10 20 30)", 60},
                {"(+ 0 0)", 0},

                // Subtraction
                {"(- 10 3)", 7},
                {"(- 20 5)", 15},
                {"(- 100 50)", 50},
                {"(- 5 5)", 0},

                // Unary minus
                {"(- 5)", -5},
                {"(- 10)", -10},
                {"(- 0)", 0},

                // Multiplication
                {"(* 2 3)", 6},
                {"(* 2 3 4)", 24},
                {"(* 5 5)", 25},
                {"(* 10 10 10)", 1000},
                {"(* 1 1)", 1},
                {"(* 0 5)", 0},

                // Division
                {"(/ 20 4)", 5},
                {"(/ 100 10)", 10},
                {"(/ 15 3)", 5},
                {"(/ 8 2)", 4},

                // Modulo
                {"(% 10 3)", 1},
                {"(% 20 5)", 0},
                {"(% 15 4)", 3},
                {"(% 7 2)", 1},

                // Greater than
                {"(> 10 5)", true},
                {"(> 20 10)", true},
                {"(> 5 10)", false},
                {"(> 5 5)", false},

                // Less than
                {"(< 3 7)", true},
                {"(< 5 10)", true},
                {"(< 10 5)", false},
                {"(< 5 5)", false},

                // Equals
                {"(= 5 5)", true},
                {"(= 10 10)", true},
                {"(= 5 10)", false},
                {"(= 0 0)", true}
        });
    }

    // Parameterized test for arithmetic and comparison operations
    @Test
    public void testExpressions() {
        Node ast = parser.parse(expression);
        Object result = ast.accept(evaluator);
        assertEquals("Expression: " + expression, expectedResult, result);
    }


}