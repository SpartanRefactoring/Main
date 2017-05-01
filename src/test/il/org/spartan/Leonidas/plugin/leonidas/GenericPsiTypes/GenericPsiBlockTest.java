package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class GenericPsiBlockTest extends PsiTypeHelper {
    PsiElement psiElement;
    GenericPsiBlock genericPsiBlock;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        genericPsiBlock = new GenericPsiBlock(psiElement);
    }

    public void testGeneralizes() throws Exception {
        PsiElement e = createTestBlockStatementFromString("{int x=0;}");
        assert genericPsiBlock.generalizes(e);
        e = createTestCodeBlockFromString("{int x=0;}");
        assert genericPsiBlock.generalizes(e);
        e = createTestStatementFromString("int x=0;");
        assert genericPsiBlock.generalizes(e);
        e = createTestExpression("x++");
        assert !genericPsiBlock.generalizes(e);
    }

    public void testToString() throws Exception {
        genericPsiBlock.putUserData(KeyDescriptionParameters.ID, 100);
        assert genericPsiBlock.toString().equals("GenericBlock100");
    }

    public void testCopy() throws Exception {
        GenericPsiBlock copyGenericPsiBlock = genericPsiBlock.copy();
        assert copyGenericPsiBlock.getInner() != psiElement;
        assert copyGenericPsiBlock.getInner().toString().equals(psiElement.toString());
        assert copyGenericPsiBlock != genericPsiBlock;
        assert copyGenericPsiBlock.toString().equals(genericPsiBlock.toString());
    }

    public void testGetProject() {
        assert genericPsiBlock.getProject().equals(psiElement.getProject());
    }
}