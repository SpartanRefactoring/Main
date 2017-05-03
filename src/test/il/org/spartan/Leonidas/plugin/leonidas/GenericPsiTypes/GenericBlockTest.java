package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.Amount;
import il.org.spartan.Leonidas.plugin.leonidas.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.GenericBlock;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class GenericBlockTest extends PsiTypeHelper {
    PsiElement psiElement;
    GenericBlock genericBlock;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        genericBlock = new GenericBlock(psiElement);
    }

    public void testGeneralizes() throws Exception {
        PsiElement e = createTestBlockStatementFromString("{int x=0;}");
        assert genericBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
        e = createTestCodeBlockFromString("{int x=0;}");
        assert genericBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
        e = createTestStatementFromString("int x=0;");
        assert genericBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
        e = createTestExpression("x++");
        assert !genericBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
    }
}