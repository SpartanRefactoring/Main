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

    @Override
    protected void tearDown() throws Exception {
        clearFields(this);
        method = null;
        super.tearDown();
    }

    public void testConformsMethods() {
        assert method.conforms(createTestMethodFromString("void method0(){}"));
        assert method.conforms(createTestMethodFromString("void method192(){}"));
        assert method.conforms(createTestMethodFromString("public static int method192(){return 0;}"));
        assert !method.conforms(createTestMethodFromString("void iDoNotStartWithMethod(){}"));
        assert !method.conforms(createTestMethodFromString("void _method321(){}"));
    }

    public void testCorrectlyExtractId() {
        assertEquals(method.extractId(createTestMethodFromString("void method0(){}")), Integer.valueOf(0));
        assertEquals(method.extractId(createTestMethodFromString("void method22(){}")), Integer.valueOf(22));
        assertEquals(method.extractId(createTestMethodFromString("public static int method55(){return 0;}")), Integer.valueOf(55));
    }

    public void testGeneralizesMethods() {
        //TODO @sharon
        Encapsulator e = Encapsulator.buildTreeFromPsi(createTestMethodFromString("public void method0() {}"));
        method = (Method) method.create(e, new HashMap<>());
        assert method.generalizes(buildTreeFromPsi(createTestMethodFromString("public void m() {}")), new HashMap<>())
				.matches();
        assert !method.generalizes(buildTreeFromPsi(createTestMethodFromString("public int m() {}")), new HashMap<>())
				.matches();
    }

    public void testDoesNotGeneralizeStatements() {
        assert !method.generalizes(buildTreeFromPsi(createTestStatementFromString("x++;")), new HashMap<>()).matches();
        assert !method.generalizes(buildTreeFromPsi(createTestStatementFromString("x = y + z;")), new HashMap<>())
				.matches();
        assert !method.generalizes(buildTreeFromPsi(createTestStatementFromString("m();")), new HashMap<>()).matches();
        assert !method.generalizes(buildTreeFromPsi(createTestStatementFromString("return 's';")), new HashMap<>())
				.matches();
    }

    public void testDoesNotGeneralizeExpressions() {
        assert !method.generalizes(buildTreeFromPsi(createTestExpression("x + y")), new HashMap<>()).matches();
        assert !method.generalizes(buildTreeFromPsi(createTestExpression("true")), new HashMap<>()).matches();
        assert !method.generalizes(buildTreeFromPsi(createTestExpression("x++")), new HashMap<>()).matches();
        assert !method.generalizes(buildTreeFromPsi(createTestExpression("a()")), new HashMap<>()).matches();
    }


    public void testGoUpwards() {
        Encapsulator e1 = buildTreeFromPsi(createTestMethodFromString("public void m() {}")),
                e2 = buildTreeFromPsi(createTestMethodFromString("public void m(int i) {}")),
                e3 = buildTreeFromPsi(createTestMethodFromString("public void m(String s) {}")),
                e4 = buildTreeFromPsi(createTestMethodFromString("public int m() {return 123}"));
        assert !method.goUpwards(e1, e1.getParent());
        assert !method.goUpwards(e2, e2.getParent());
        assert !method.goUpwards(e3, e3.getParent());
        assert !method.goUpwards(e4, e4.getParent());
    }
}
