package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericAnyBlock;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class GenericBlockTest extends PsiTypeHelper {
    PsiElement psiElement;
    GenericAnyBlock genericAnyBlock;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        genericAnyBlock = new GenericAnyBlock(psiElement);
    }

    public void testGeneralizes() throws Exception {
        PsiElement e = createTestBlockStatementFromString("{int x=0;}");
        assert genericAnyBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
        e = createTestCodeBlockFromString("{int x=0;}");
        assert genericAnyBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
        e = createTestStatementFromString("int x=0;");
        assert genericAnyBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
        e = createTestExpression("x++");
        assert !genericAnyBlock.generalizes(Encapsulator.buildTreeFromPsi(e));
    }
}