package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiBinaryExpression;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author  Amir Sagiv
 * @since 17-01-2017.
 */
public class hazTest extends PsiTypeHelper {

    public void testHazCentVariableDefinition() throws Exception {
        assert haz.centVariableDefinition(createTestStatementFromString("int ¢ = 5;"));
        assert haz.centVariableDefinition(createTestBlockStatementFromString("{int x = 5; int ¢ = 5;}"));
        assert !haz.centVariableDefinition(createTestStatementFromString("int x = 5;"));
        assert haz.centVariableDefinition(createTestMethodFromString("public static int getSomething(){int ¢ = 5; return ¢;}"));
        assert haz.centVariableDefinition(createTestClassFromString("", "A", "private int ¢;", "public"));
    }

    public void testHazFunctionNamed() throws Exception{
        assert haz.functionNamed(createTestMethodFromString("public int getX(){return 1;}"), "getX");
        assert haz.functionNamed(
                createTestClassFromString("", "A", "pubic A(){} private static int getX(){return 1;}", "public"),
                "getX");
        assert !haz.functionNamed(
                createTestClassFromString("", "A", "pubic A(){} private static int getY(){return 1;}", "public"),
                "getX");
        assert haz.functionNamed(createTestInterfaceFromString("", "A", "private static int getX();", "public"),
				"getX");

    }

    public void testHazEqualsOperator() throws Exception{
        assert haz.equalsOperator((PsiBinaryExpression) createTestExpression("x == y"));
        assert !haz.equalsOperator((PsiBinaryExpression) createTestExpression("x != y"));
        assert !haz.equalsOperator((PsiBinaryExpression) createTestExpression("x > y"));
    }

    public void testHazNotEqualsOperator() throws Exception{
        assert !haz.notEqualsOperator((PsiBinaryExpression) createTestExpression("x == y"));
        assert haz.notEqualsOperator((PsiBinaryExpression) createTestExpression("x != y"));
        assert !haz.notEqualsOperator((PsiBinaryExpression) createTestExpression("x > y"));
    }

    public void testHasPublicModifier() throws Exception {
        assert haz.publicModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("public int x =  1;")));
    }

    public void testDoesNotHavePublicModifier() throws Exception {
        assert !haz.publicModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("private int x =  1;")));
    }

    public void testHasPrivateModifier() throws Exception {
        assert haz.privateModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("private int x =  1;")));
    }

    public void testDoesNotHavePrivateModifier() throws Exception {
        assert !haz.privateModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("int x =  1;")));
    }

    public void testHasProtectedModifier() throws Exception {
        assert haz
                .protectedModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("protected int x =  1;")));
    }

    public void testDoesNotHaveProtectedModifier() throws Exception {
        assert !haz.protectedModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("int x =  1;")));
    }

    public void testHasStaticModifier() throws Exception {
        assert haz.staticModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("static int x =  1;")));
    }

    public void testDoesNotHaveStaticModifier() throws Exception {
        assert !haz.staticModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("int x =  1;")));
    }

    public void testHasFinalModifier() throws Exception {
        assert haz.finalModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("final int x =  1;")));
    }

    public void testDoesNotHaveFinalModifier() throws Exception {
        assert !haz.finalModifier(az.fieldDeclaration(createTestFieldDeclarationFromString("int x =  1;")));
    }
}