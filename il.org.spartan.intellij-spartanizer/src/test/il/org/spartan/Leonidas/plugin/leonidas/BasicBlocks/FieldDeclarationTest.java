package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.PsiTypeHelper;

import java.util.HashMap;

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

    @Override
    protected void tearDown() throws Exception {
        clearFields(this);
        fieldDeclaration = null;
        super.tearDown();
    }

    public void testFieldDeclaration() {
        assert fieldDeclaration
				.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int x;")),
						new HashMap<>())
				.matches();
        assert fieldDeclaration
				.generalizes(Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int x, y;")),
						new HashMap<>())
				.matches();
        assert fieldDeclaration.generalizes(
				Encapsulator.buildTreeFromPsi(createTestFieldDeclarationFromString("int[] x = new int[5];")),
				new HashMap<>()).matches();
        assert fieldDeclaration.generalizes(
				Encapsulator.buildTreeFromPsi(
						createTestFieldDeclarationFromString("public static final String s = \"dsa\"")),
				new HashMap<>()).matches();
        assert !fieldDeclaration.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("x")), new HashMap<>())
				.matches();
        assert !fieldDeclaration
				.generalizes(Encapsulator.buildTreeFromPsi(createTestExpression("true")), new HashMap<>()).matches();
        assert !fieldDeclaration
				.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("int a() {return 0;}")),
						new HashMap<>())
				.matches();
        assert !fieldDeclaration
				.generalizes(Encapsulator.buildTreeFromPsi(createTestMethodFromString("void a(int x) {}")),
						new HashMap<>())
				.matches();
    }
}
