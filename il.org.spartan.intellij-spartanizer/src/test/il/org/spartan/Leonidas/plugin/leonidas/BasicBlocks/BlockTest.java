package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Anna Belozovsky
 * @since 01/05/2017
 */
public class BlockTest extends PsiTypeHelper {
    PsiElement psiElement;
    Block block;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        psiElement = createTestStatementFromString("int x;");
        block = new Block(psiElement);
    }

    public void testGeneralizes() throws Exception {
        PsiElement e = createTestBlockStatementFromString("{int x=0;}");
        assert block.generalizes(Encapsulator.buildTreeFromPsi(e)).matches();
        e = createTestCodeBlockFromString("{int x=0;}");
        assert block.generalizes(Encapsulator.buildTreeFromPsi(e)).matches();
        e = createTestStatementFromString("int x=0;");
        assert block.generalizes(Encapsulator.buildTreeFromPsi(e)).matches();
        e = createTestExpression("x++");
        assert !block.generalizes(Encapsulator.buildTreeFromPsi(e)).matches();
    }
}