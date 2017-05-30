package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Sharon LK
 */
public class FieldDeclarationTest extends PsiTypeHelper {
    private FieldDeclaration fieldDeclaration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fieldDeclaration = new FieldDeclaration();
    }

    public void testFieldDeclaration() {
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int x;"))).matches());
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int x, y;"))).matches());
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int[] x = new int[5];"))).matches());
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("public static final String s = \"dsa\""))).matches());
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x"))).matches());
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true"))).matches());
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("int a() {return 0;}"))).matches());
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("void a(int x) {}"))).matches());
    }
}
