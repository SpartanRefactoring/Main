package il.org.spartan.Leonidas.plugin;

import il.org.spartan.Leonidas.PsiTypeHelper;
import org.junit.Assert;

/**
 * @author melanyc, RoeiRaz
 * @since 29/4/17
 */
public class EncapsulatingNodeTest extends PsiTypeHelper {
    private final String ifStatement1 = "" +
            "if (expression) {" +
            "   statement();" +
            "}";

    public void testRootEncapsulatingNodeIsOrphan() throws Exception {
        EncapsulatingNode node = EncapsulatingNode.buildTreeFromPsi(createTestStatementFromString(ifStatement1));
        Assert.assertNull(node.getParent());
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