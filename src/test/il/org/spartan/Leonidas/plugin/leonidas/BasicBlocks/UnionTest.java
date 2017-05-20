package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethodCallExpression;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Sharon LK
 * @since 20.5.17
 */
public class UnionTest extends PsiTypeHelper {
    protected PsiMethodCallExpression methodCallExpression(String s) {
        return (PsiMethodCallExpression) createTestExpression(s);
    }

    public void testUnionOfStatement() {
        PsiElement element = methodCallExpression("union(0, statement(1))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("System.out.println();")));

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("1 + 2"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestClassFromString("class XX {}"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void a()"))));
    }

    public void testUnionOfStatementAndMethod() {
        PsiElement element = methodCallExpression("union(0, statement(1), method(2))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("int a() {return 0;}")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public int b(double x) {return x - 1;}")));

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("1 + 2"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestClassFromString("class XX {}"))));
    }

    public void testUnionOfBooleanExpressionAndBooleanLiteral() {
        PsiElement element = methodCallExpression("union(0, booleanExpression(1), booleanLiteral(2))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("3 > 5")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false && x <= 10")));

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("3"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("41.4"))));
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;"))));
    }
}
