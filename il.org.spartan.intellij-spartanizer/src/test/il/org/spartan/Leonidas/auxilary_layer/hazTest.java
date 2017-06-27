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
        assert haz.functionNamed(createTestClassFromString("", "A", "pubic A(){} private static int getX(){return 1;}", "public"), "getX");
        assert !haz.functionNamed(createTestClassFromString("", "A", "pubic A(){} private static int getY(){return 1;}", "public"), "getX");
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
}