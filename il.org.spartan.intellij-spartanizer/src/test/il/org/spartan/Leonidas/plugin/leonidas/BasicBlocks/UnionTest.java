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

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("System.out.println();"))).matches();

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("1 + 2"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestClassFromString("class XX {}"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void a()"))).matches());
    }

    public void testUnionOfStatementAndMethod() {
        PsiElement element = methodCallExpression("union(0, statement(1), method(2))");
        Union union = new Union(element);
//TODO @sharon
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;"))).matches();
//        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("int a() {return 0;}")));
//        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public int b(double x) {return x - 1;}")));

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("1 + 2"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestClassFromString("class XX {}"))).matches());
    }

    public void testUnionOfStatementAndBooleanLiteral() {
        PsiElement element = methodCallExpression("union(0, statement(1), booleanLiteral(2))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x, y;"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("someMethod();"))).matches();

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("3"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("41.4"))).matches());
    }

    public void testMultipleUnionsOfSameThingWorkAsExpected() {
        PsiElement element = methodCallExpression("union(0, statement(1), booleanLiteral(3), statement(1), booleanLiteral(4), booleanLiteral(5))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x, y;"))).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("someMethod();"))).matches();

        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("3"))).matches());
        assertFalse(union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("41.4"))).matches());
    }
}
