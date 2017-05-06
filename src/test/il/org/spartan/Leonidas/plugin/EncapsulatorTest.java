package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;

/**
 * @author melanyc, RoeiRaz
 * @since 29/4/17
 */
public class EncapsulatorTest extends PsiTypeHelper {
    private final String ifStatement1 = "" +
            "if (expression) {" +
            "   statement();" +
            "}";

    private boolean matchNodeTreeAndPsiTreeByReference(Encapsulator node, PsiElement e) {
        if (node == e && e == null)
            return true;

        if (node.getInner() != e)
            return false;

        if (node.getChildren().size() != e.getChildren().length)
            return false;

        for (int i = 0; i < node.getChildren().size(); i++)
            if (!matchNodeTreeAndPsiTreeByReference(node.getChildren().get(i), e.getChildren()[i])) return false;

        return true;
    }

    @Before
    public void setup() {
        PsiRewrite mockPsiRewrite = Mockito.mock(PsiRewrite.class);
    }

    public void testRootEncapsulatingNodeIsOrphan() throws Exception {
        Encapsulator node = Encapsulator.buildTreeFromPsi(createTestStatementFromString(ifStatement1));
        Assert.assertNull(node.getParent());
    }

    public void testTreeBuiltFromPsiElementConformsToPsiElement() {
        PsiElement ifStatement1Psi = createTestStatementFromString(ifStatement1);
        Encapsulator node = Encapsulator.buildTreeFromPsi(ifStatement1Psi);
        Assert.assertTrue(matchNodeTreeAndPsiTreeByReference(node, ifStatement1Psi));
    }

    public void testReplace() throws Exception {

    }

    public void testGetChildren() throws Exception {

    }

    public void testGetParent() throws Exception {

    }

    public void testAccept() throws Exception {

    }

    public void testAccept1() throws Exception {

    }

    public void testSetInner() throws Exception {

    }

    public void testGetAmountOfNoneWhiteSpaceChildren() throws Exception {

    }

    public void testToString() throws Exception {

    }

    public void testGetText() throws Exception {

    }

    public void testClone() throws Exception {

    }

    public void testForEach() throws Exception {

    }

}