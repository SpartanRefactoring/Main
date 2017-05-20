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

    public void testUnionOfMethodAndStatement() {
        PsiElement element = methodCallExpression("union(0, statement(1))");
        Union union = new Union(element);

        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")));
        assert union.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("System.out.println();")));
    }
}
