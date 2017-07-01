package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.HashMap;

/**
 * @author Sharon LK
 */
public class IdentifiableTest extends PsiTypeHelper {
    Identifiable id;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        id = new Identifier();
    }

    @Override
    protected void tearDown() throws Exception {
        clearFields(this);
        id = null;
        super.tearDown();
    }

    public void testConforms() {
        assert id.conforms(createTestIdentifierFromString("identifier1"));
        assert id.conforms(createTestIdentifierFromString("identifier2"));
        assert id.conforms(createTestIdentifierFromString("identifier3"));
        assert !id.conforms(createTestIdentifierFromString("x"));
        assert !id.conforms(createTestIdentifierFromString("some_id"));
        assert !id.conforms(createTestStatementFromString("identifier1++;"));
    }

    public void testGeneralizes() {
        assert id.generalizes(Encapsulator.buildTreeFromPsi(createTestIdentifierFromString("x")), new HashMap<>())
				.matches();
        assert id.generalizes(Encapsulator.buildTreeFromPsi(createTestIdentifierFromString("y")), new HashMap<>())
				.matches();
        assert !id.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")), new HashMap<>())
				.matches();
        assert !id.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("true && false")),
				new HashMap<>()).matches();
        assert !id.generalizes(Encapsulator.buildTreeFromPsi(createTestExpressionFromString("x + 2")), new HashMap<>())
				.matches();
    }
}
