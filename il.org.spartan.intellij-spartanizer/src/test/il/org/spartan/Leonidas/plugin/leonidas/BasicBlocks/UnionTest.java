package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethodCallExpression;
import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.HashMap;

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

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("System.out.println();")), new HashMap<>()).matches();

        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++")), new HashMap<>())
				.matches();
        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("1 + 2")), new HashMap<>())
				.matches();
        assert !union
				.generalizes(Encapsulator.buildTreeFromPsi(createTestClassFromString("class XX {}")), new HashMap<>())
				.matches();
        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void a()")),
				new HashMap<>()).matches();
    }

    public void testUnionOfStatementAndMethod() {
        PsiElement element = methodCallExpression("union(0, statement(1), method(2))");
        Union union = new Union(element);
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")), new HashMap<>()).matches();
        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++")), new HashMap<>())
				.matches();
        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("1 + 2")), new HashMap<>())
				.matches();
        assert !union
				.generalizes(Encapsulator.buildTreeFromPsi(createTestClassFromString("class XX {}")), new HashMap<>())
				.matches();
    }

    public void testUnionOfStatementAndBooleanLiteral() {
        PsiElement element = methodCallExpression("union(0, statement(1), booleanLiteral(2))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x, y;")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("someMethod();")), new HashMap<>()).matches();

        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("3")), new HashMap<>()).matches();
        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("41.4")), new HashMap<>())
				.matches();
    }

    public void testMultipleUnionsOfSameThingWorkAsExpected() {
        PsiElement element = methodCallExpression("union(0, statement(1), booleanLiteral(3), statement(1), booleanLiteral(4), booleanLiteral(5))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x, y;")), new HashMap<>()).matches();
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("someMethod();")), new HashMap<>()).matches();

        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("3")), new HashMap<>()).matches();
        assert !union.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("41.4")), new HashMap<>())
				.matches();
    }
}
