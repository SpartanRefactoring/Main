package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Sharon
 * @since 14.5.17
 */
public class GenericBooleanLiteralTest extends PsiTypeHelper {
    protected BooleanLiteral booleanLiteral;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        booleanLiteral = new BooleanLiteral();
    }

    public void testGeneralizesBooleanLiterals() {
        assert booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")));
        assert booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("false")));
    }
}
