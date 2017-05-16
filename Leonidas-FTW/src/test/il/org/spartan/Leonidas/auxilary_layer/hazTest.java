package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author  Amir Sagiv
 * @since 17-01-2017.
 */
public class hazTest extends PsiTypeHelper {

    public void testHazCentVariableDefinition() throws Exception {
        PsiElement e1 = createTestStatementFromString("int ¢ = 5;");
        assert haz.centVariableDefinition(e1);
        PsiElement e2 = createTestBlockStatementFromString("{int x = 5; int ¢ = 5;}");
        assert haz.centVariableDefinition(e2);
        PsiElement e3 = createTestStatementFromString("int x = 5;");
        assert !haz.centVariableDefinition(e3);
        PsiElement e4 = createTestMethodFromString("public static int getSomething(){int ¢ = 5; return ¢;}");
        assert haz.centVariableDefinition(e4);
        assert haz.centVariableDefinition(createTestClassFromString("", "A", "private int ¢;", "public"));
    }

    public void testHazFunctionNamed() throws Exception{
        PsiElement e1 = createTestMethodFromString("public int getX(){return 1;}");
        assert haz.functionNamed(e1, "getX");
        PsiElement e2 = createTestClassFromString("","A","pubic A(){} private static int getX(){return 1;}","public");
        assert haz.functionNamed(e2, "getX");
        PsiElement e3 = createTestClassFromString("","A","pubic A(){} private static int getY(){return 1;}","public");
        assert !haz.functionNamed(e3, "getX");
        assert haz.functionNamed(createTestInterfaceFromString("", "A", "private static int getX();", "public"),
				"getX");

    }

    public void testHazEqualsOperator() throws Exception{
        PsiBinaryExpression e1 = (PsiBinaryExpression) createTestExpression("x == y");
        assert haz.equalsOperator(e1);
        PsiBinaryExpression e2 = (PsiBinaryExpression) createTestExpression("x != y");
        assert !haz.equalsOperator(e2);
        assert !haz.equalsOperator((PsiBinaryExpression) createTestExpression("x > y"));
    }

    public void testHazNotEqualsOperator() throws Exception{
        PsiBinaryExpression e1 = (PsiBinaryExpression) createTestExpression("x == y");
        assert !haz.notEqualsOperator(e1);
        PsiBinaryExpression e2 = (PsiBinaryExpression) createTestExpression("x != y");
        assert haz.notEqualsOperator(e2);
        assert !haz.notEqualsOperator((PsiBinaryExpression) createTestExpression("x > y"));
    }
}