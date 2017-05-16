package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author michalcohen
 * @since 12-01-2017
 */
public class typeTest extends PsiTypeHelper {
    public void testCallExpression() {
        assertEquals(type.of(createTestNewExpression("Object", "")), PsiCallExpression.class);
    }

    public void testMethodReferenceExpression() {
        assertEquals(type.of(createTestMethodReferenceEpression("Object", "toString")),
                PsiMethodReferenceExpression.class);
    }

    public void testMethodCallExpression() {
        assertEquals(type.of(createTestMethodCallExpression("banana", "5")), PsiMethodCallExpression.class);
    }

    public void testMethod() {
        assertEquals(type.of(createTestMethodFromString("int foo() { return 5; }")), PsiMethod.class);
    }

    public void testConditionalExpression() {
        assertEquals(type.of(createTestConditionalExpression("x > 0", "null", "x")), PsiConditionalExpression.class);
    }

    public void testIdentifier() {
        assertEquals(type.of(createTestIdentifierFromString("banana")), PsiIdentifier.class);
    }

    public void testForeachStatement() {
        assertEquals(type.of(createTestForeachStatementFromString("for(Object x : list) { x.toString(); }")),
                PsiForeachStatement.class);
    }

    public void testLambdaExpression() {
        assertEquals(type.of(createTestLambdaExpression("x -> x + 2")), PsiLambdaExpression.class);
    }

    public void testIfStatement() {
        assertEquals(type.of(createTestIfStatement("x > 0", "return 5")), PsiIfStatement.class);
    }

    public void testWhileStatement() {
        assertEquals(type.of(createTestWhileStatementFromString("while(x > 0) { x--; }")), PsiWhileStatement.class);
    }
}