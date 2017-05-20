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
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int x;"))));
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int x, y;"))));
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int[] x = new int[5];"))));
        assertTrue(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("public static final String s = \"dsa\""))));
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x"))));
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true"))));
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("int a() {return 0;}"))));
        assertFalse(fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("void a(int x) {}"))));
    }
}
