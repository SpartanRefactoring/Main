package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

/**
 * @author melanyc, RoeiRaz
 * @since 29/4/17
 */
public class EncapsulatorTest extends PsiTypeHelper {
    private PsiIfStatement ifStatement1Psi;
    private Encapsulator node;
    private PsiRewrite mockPsiRewrite = Mockito.mock(PsiRewrite.class);


    @Override
    public void setUp() throws Exception {
        super.setUp();
        ifStatement1Psi = createTestIfStatement("booleanExpression(0)", "statement(1);");
        node = Encapsulator.buildTreeFromPsi(ifStatement1Psi);

    }

    private boolean matchNodeTreeAndPsiTreeByReference(Encapsulator node, PsiElement e) {
        if (node == null && e == null) return true;
        if (node == null ^ e == null) return false;
        if (node.getInner() != e || node.getChildren().size() != e.getChildren().length)
            return false;
        for (int i = 0; i < node.getChildren().size(); i++)
            if (!matchNodeTreeAndPsiTreeByReference(node.getChildren().get(i), e.getChildren()[i])) return false;
        return true;
    }

    public void testRootEncapsulatingNodeIsOrphan() throws Exception {
        String ifStatement1 = "" +
                "if (booleanExpression(0)) {" +
                "   statement(1);" +
                "}";
        Encapsulator node = Encapsulator.buildTreeFromPsi(createTestStatementFromString(ifStatement1));
        Assert.assertNull(node.getParent());
    }

    public void testTreeBuiltFromPsiElementConformsToPsiElement() {
        Assert.assertTrue(matchNodeTreeAndPsiTreeByReference(node, ifStatement1Psi));
    }

    public void testReplace() throws Exception {

    }

    public void testGetChildren() throws Exception {
        Encapsulator node2 = Encapsulator.buildTreeFromPsi(createTestAnnotationFromString("@Banana(\"honey\")"));
        assertEquals(node.getChildren().size(), 7);
        assertEquals(node2.getChildren().size(), 3);
    }

    public void testGetParent() throws Exception {
        assertEquals(node.getChildren().get(0).getParent(), node);
        assertFalse(node.getChildren().get(3).getChildren().get(0).getParent() == node);
    }

    public void testAccept() throws Exception {
        List<Integer> ids = new LinkedList<>();
        node.accept(n -> {
            if (iz.literal(n.getInner())) {
                ids.add(Integer.parseInt(az.literal(n.getInner()).getText()));
            }
        });
        assertTrue(ids.contains(0));
        assertTrue(ids.contains(1));
        assertFalse(ids.contains(-1));
        assertFalse(ids.contains(2));
    }

    public void testGetAmountOfActualChildren() {
        Encapsulator node2 = Encapsulator.buildTreeFromPsi(createTestAnnotationFromString("@Banana(\"honey\")"));
        assertEquals(node.getAmountOfActualChildren(), 5);
        assertEquals(node2.getAmountOfActualChildren(), 3);
    }

    public void testToString() {
        assertEquals("PsiIfStatement", node + "");
    }

    public void testGetText() {
        assertEquals("if (booleanExpression(0)) {statement(1);}", node.getText());
    }

    public void testPutAndGetId() {
        node.putId(3);
        assertTrue(node.getId().compareTo(3) == 0);
    }

    public void testGeneralizeWith() {
        Encapsulator rep = Encapsulator.buildTreeFromPsi(createTestExpression("x++"));
        node.getChildren().get(3).generalizeWith(rep);
        assertEquals(node.getChildren().get(3).getText(), "x++");
    }

    public void testIsGeneric() {
        assertFalse(node.isGeneric());
    }
}