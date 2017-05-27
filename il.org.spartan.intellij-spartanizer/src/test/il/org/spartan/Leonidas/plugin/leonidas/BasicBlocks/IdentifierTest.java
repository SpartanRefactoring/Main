package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Sharon LK
 */
public class IdentifierTest extends PsiTypeHelper {
    Identifier id;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        id = new Identifier();
    }

    public void testConforms() {
        assertTrue(id.conforms(createTestIdentifierFromString("identifier1")));
        assertTrue(id.conforms(createTestIdentifierFromString("identifier2")));
        assertTrue(id.conforms(createTestIdentifierFromString("identifier3")));
        assertFalse(id.conforms(createTestIdentifierFromString("x")));
        assertFalse(id.conforms(createTestIdentifierFromString("some_id")));
        assertFalse(id.conforms(createTestStatementFromString("identifier1++;")));
    }

    public void testGeneralizes() {
        assertTrue(id.generalizes(Encapsulator.buildTreeFromPsi(createTestIdentifierFromString("x"))));
        assertTrue(id.generalizes(Encapsulator.buildTreeFromPsi(createTestIdentifierFromString("y"))));
        assertFalse(id.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;"))));
        assertFalse(id.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("true && false"))));
        assertFalse(id.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("x + 2"))));
    }
}
