package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.HashMap;

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

    @Override
    protected void tearDown() throws Exception {
        clearFields(this);
        block = null;
        psiElement = null;
        super.tearDown();
    }

    public void testGeneralizes() throws Exception {
        PsiElement e = createTestBlockStatementFromString("{int x=0;}");
        assert block.generalizes(Encapsulator.buildTreeFromPsi(e), new HashMap<>()).matches();
        e = createTestCodeBlockFromString("{int x=0;}");
        assert block.generalizes(Encapsulator.buildTreeFromPsi(e), new HashMap<>()).matches();
        e = createTestStatementFromString("int x=0;");
        assert block.generalizes(Encapsulator.buildTreeFromPsi(e), new HashMap<>()).matches();
        e = createTestExpression("x++");
        assert !block.generalizes(Encapsulator.buildTreeFromPsi(e), new HashMap<>()).matches();
    }
}