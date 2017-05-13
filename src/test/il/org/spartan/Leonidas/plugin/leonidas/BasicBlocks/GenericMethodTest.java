package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class GenericMethodTest extends PsiTypeHelper {
    protected Method method;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        method = new Method("");
    }

    public void testGeneralizesMethods() {
        assert method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m() {}")));
        assert method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m(int i) {}")));
        assert method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m(String s) {}")));
        assert method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public int m() {return 123}")));
    }

    public void testDoesNotGeneralizeStatements() {
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;")));
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x = y + z;")));
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("m();")));
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("return 's';")));
    }

    public void testDoesNotGeneralizeExpressions() {
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x + y")));
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")));
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++")));
        assert !method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("a()")));
    }
}
