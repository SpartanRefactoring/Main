package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Expression;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class ExpressionTest extends PsiTypeHelper {
    PsiType psiType;
    PsiElement psiElement;
    Expression expression;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        psiType = createTestTypeFromString("int");
        expression = new Expression(psiElement, psiType);
    }

    public void testEvaluationType() throws Exception {
        assert expression.evaluationType().equals(psiType);
    }

    public void testGeneralizes() throws Exception {
        assert expression.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("x + 1")));
        assert !expression.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")));
    }
}