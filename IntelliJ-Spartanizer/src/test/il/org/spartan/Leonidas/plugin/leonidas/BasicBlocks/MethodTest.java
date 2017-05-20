package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class MethodTest extends PsiTypeHelper {
    protected Method method;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        method = new Method();
    }

    public void testGeneralizesMethods() {
        assertTrue(method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m() {}"))));
        assertTrue(method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m(int i) {}"))));
        assertTrue(method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m(String s) {}"))));
        assertTrue(method.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("public int m() {return 123}"))));
    }

    public void testDoesNotGeneralizeStatements() {
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x++;"))));
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("x = y + z;"))));
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("m();"))));
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestStatementFromString("return 's';"))));
    }

    public void testDoesNotGeneralizeExpressions() {
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x + y"))));
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true"))));
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x++"))));
        assertFalse(method.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("a()"))));
    }


    public void testGoUpwards() {
        Encapsulator e1 = Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m() {}"));
        Encapsulator e2 = Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m(int i) {}"));
        Encapsulator e3 = Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void m(String s) {}"));
        Encapsulator e4 = Encapsulator.buildTreeFromPsi(createTestMethodFromString("public int m() {return 123}"));
        assertFalse(method.goUpwards(e1, e1.getParent()));
        assertFalse(method.goUpwards(e2, e2.getParent()));
        assertFalse(method.goUpwards(e3, e3.getParent()));
        assertFalse(method.goUpwards(e4, e4.getParent()));
    }

    public void testCreate() {
        assertEquals(method.create(Encapsulator.buildTreeFromPsi(createTestMethodCallExpression("method", "0"))).getText(), "method(0)");
    }
}
