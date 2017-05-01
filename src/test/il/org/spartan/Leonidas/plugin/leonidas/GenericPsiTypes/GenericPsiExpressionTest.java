package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class GenericPsiExpressionTest extends PsiTypeHelper {
    PsiType psiType;
    PsiElement psiElement;
    GenericPsiExpression genericPsiExpression;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        psiType = createTestTypeFromString("int");
        genericPsiExpression = new GenericPsiExpression(psiType, psiElement);
    }

    public void testToString() throws Exception {
        assert genericPsiExpression.toString().equals("Generic expressionnull");
        genericPsiExpression.putUserData(KeyDescriptionParameters.ID, 100);
        assert genericPsiExpression.toString().equals("Generic expression100");
    }

    public void testEvaluationType() throws Exception {
        assert genericPsiExpression.evaluationType().equals(psiType);
    }

    public void testGeneralizes() throws Exception {
        assert genericPsiExpression.generalizes(createTestExpressionFromString("x + 1"));
        assert !genericPsiExpression.generalizes(createTestStatementFromString("int x;"));
    }

    public void testCopy() throws Exception {
        GenericPsiExpression copyGenericPsiExpression = genericPsiExpression.copy();
        assert copyGenericPsiExpression != genericPsiExpression;
        assert copyGenericPsiExpression.toString().equals(genericPsiExpression.toString());
        assert copyGenericPsiExpression.getInner() != psiElement;
        assert copyGenericPsiExpression.getInner().toString().equals(psiElement.toString());
        assert copyGenericPsiExpression.evaluationType().toString().equals(genericPsiExpression.evaluationType().toString());
    }

}