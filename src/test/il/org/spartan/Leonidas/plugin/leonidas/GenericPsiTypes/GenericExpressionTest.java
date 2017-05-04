package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericExpression;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class GenericExpressionTest extends PsiTypeHelper {
    PsiType psiType;
    PsiElement psiElement;
    GenericExpression genericExpression;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        psiType = createTestTypeFromString("int");
        genericExpression = new GenericExpression(psiElement, psiType);
    }

    public void testEvaluationType() throws Exception {
        assert genericExpression.evaluationType().equals(psiType);
    }

    public void testGeneralizes() throws Exception {
        assert genericExpression.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("x + 1")));
        assert !genericExpression.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;")));
    }
}