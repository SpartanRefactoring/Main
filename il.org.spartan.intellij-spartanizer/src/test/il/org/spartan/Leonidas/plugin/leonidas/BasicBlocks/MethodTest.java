package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.HashMap;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator.buildTreeFromPsi;

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

    public void testConformsMethods() {
        assertTrue(method.conforms(createTestMethodFromString("void method0(){}")));
        assertTrue(method.conforms(createTestMethodFromString("void method192(){}")));
        assertTrue(method.conforms(createTestMethodFromString("public static int method192(){return 0;}")));
        assertFalse(method.conforms(createTestMethodFromString("void iDoNotStartWithMethod(){}")));
        assertFalse(method.conforms(createTestMethodFromString("void _method321(){}")));
    }

    public void testCorrectlyExtractId() {
        assertEquals(method.extractId(createTestMethodFromString("void method0(){}")), new Integer(0));
        assertEquals(method.extractId(createTestMethodFromString("void method22(){}")), new Integer(22));
        assertEquals(method.extractId(createTestMethodFromString("public static int method55(){return 0;}")), new Integer(55));
    }

    public void testGeneralizesMethods() {
        //TODO @sharon
        Encapsulator e = Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void method0() {}"));
        method = (Method) method.create(e, new HashMap<>());
        assertTrue(method.generalizes(buildTreeFromPsi(createTestMethodFromString("public void m() {}")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestMethodFromString("public int m() {}")), new HashMap<>()).matches());
//        assertTrue(method.generalizes(buildTreeFromPsi(createTestMethodFromString("public void m(int i) {}"))));
//        assertTrue(method.generalizes(buildTreeFromPsi(createTestMethodFromString("public void m(String s) {}"))));
//        assertTrue(method.generalizes(buildTreeFromPsi(createTestMethodFromString("public int m() {return 123}"))));
    }

    public void testDoesNotGeneralizeStatements() {
        //TODO @sharon
        assertFalse(method.generalizes(buildTreeFromPsi(createTestStatementFromString("x++;")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestStatementFromString("x = y + z;")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestStatementFromString("m();")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestStatementFromString("return 's';")), new HashMap<>()).matches());
    }

    public void testDoesNotGeneralizeExpressions() {
        //TODO @sharon
        assertFalse(method.generalizes(buildTreeFromPsi(createTestExpression("x + y")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestExpression("true")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestExpression("x++")), new HashMap<>()).matches());
        assertFalse(method.generalizes(buildTreeFromPsi(createTestExpression("a()")), new HashMap<>()).matches());
    }


    public void testGoUpwards() {
        Encapsulator e1 = buildTreeFromPsi(createTestMethodFromString("public void m() {}"));
        Encapsulator e2 = buildTreeFromPsi(createTestMethodFromString("public void m(int i) {}"));
        Encapsulator e3 = buildTreeFromPsi(createTestMethodFromString("public void m(String s) {}"));
        Encapsulator e4 = buildTreeFromPsi(createTestMethodFromString("public int m() {return 123}"));
        assertFalse(method.goUpwards(e1, e1.getParent()));
        assertFalse(method.goUpwards(e2, e2.getParent()));
        assertFalse(method.goUpwards(e3, e3.getParent()));
        assertFalse(method.goUpwards(e4, e4.getParent()));
    }
}
