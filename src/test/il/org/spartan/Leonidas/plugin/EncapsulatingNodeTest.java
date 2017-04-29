package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;

/**
 * @author melanyc, RoeiRaz
 * @since 29/4/17
 */
public class EncapsulatingNodeTest extends PsiTypeHelper {
    private final String ifStatement1 = "" +
            "if (expression) {" +
            "   statement();" +
            "}";

    private PsiRewrite mockPsiRewrite;

    private boolean matchNodeTreeAndPsiTreeByReference(EncapsulatingNode node, PsiElement e) {
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
        mockPsiRewrite = Mockito.mock(PsiRewrite.class);
    }

    public void testRootEncapsulatingNodeIsOrphan() throws Exception {
        EncapsulatingNode node = EncapsulatingNode.buildTreeFromPsi(createTestStatementFromString(ifStatement1));
        Assert.assertNull(node.getParent());
    }

    public void testTreeBuiltFromPsiElementConformsToPsiElement() {
        PsiElement ifStatement1Psi = createTestStatementFromString(ifStatement1);
        EncapsulatingNode node = EncapsulatingNode.buildTreeFromPsi(ifStatement1Psi);
        Assert.assertTrue(matchNodeTreeAndPsiTreeByReference(node, ifStatement1Psi));
    }

    public void testFailWhenInvokingReplaceOnNonGenericNode() {
        // junit 3 doesn't have humane mechanisms for exceptions testing :( :(
        try {
            EncapsulatingNode.buildTreeFromPsi(createTestStatementFromString(ifStatement1)).replace(
                    EncapsulatingNode.buildTreeFromPsi(createTestStatementFromString(ifStatement1)),
                    mockPsiRewrite
            );
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        } catch (Throwable t) {
            Assert.fail();
        }
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