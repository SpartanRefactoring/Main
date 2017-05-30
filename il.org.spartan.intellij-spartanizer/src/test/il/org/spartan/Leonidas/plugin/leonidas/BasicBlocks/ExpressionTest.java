package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.PsiTypeHelper;

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
        assert expression.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("x + 1"))).matches();
        assert !expression.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("int x;"))).matches();
    }
}