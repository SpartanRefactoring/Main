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

    public void testDoesNotGeneralizeStatements() {
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x = y + z;")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("m();")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("return 's';")));
    }

    public void testDoesNotGeneralizeOtherExpressions() {
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x + y")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("'a'")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("2")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("null")));
        assert !booleanLiteral.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("a()")));
    }
}
