package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiDeclarationStatementImpl;
import il.org.spartan.Leonidas.PsiTypeHelper;
import org.junit.Assert;

import java.util.List;

import static com.intellij.psi.JavaTokenType.PLUS;

/**
 * @author Oren Afek, Roei Raz
 * @since 10-01-2017
 */
public class stepTest extends PsiTypeHelper {

    public void testDocCommentA() {
        assertEquals(step.docCommentString(createTestClassFromString("", "A", "", "public")), "");
    }

    public void testDocCommentB() {
        assertEquals(step.docCommentString(getClassInFile(createTestFileFromString("/**javadoc*/class A {}"))),
                "javadoc");
    }

    public void testDocCommentC() {
        assertEquals(
                step.docCommentString(createTestClassFromString("", "A", "/** javadoc */void foo(){}", "public")), "");
    }

    public void testFirstParameter() {
        PsiParameter parameter = step.firstParameter(createTestMethodFromString("public void foo(int a, int b, int c) {}").getParameterList());

        assertNotNull(parameter);
        assertEquals(parameter.getType(), PsiType.INT);
        assertEquals(parameter.getName(), "a");

        assertNull(step.firstParameter(createTestMethodFromString("public void foo() {}")));
        assertNull(step.firstParameter(createTestMethodFromString("public void foo() {}").getParameterList()));
    }

    public void testSecondParameter() {
        PsiParameter parameter = step.secondParameter(createTestMethodFromString("public void foo(int a, int b, int c) {}").getParameterList());

        assertNotNull(parameter);
        Assert.assertEquals(parameter.getType(), PsiType.INT);
        Assert.assertEquals(parameter.getName(), "b");
        Assert.assertNull(step.secondParameter(createTestMethodFromString("public void foo(int a) {}").getParameterList()));
    }

    public void testArguments() {
        assertNull(step.arguments(null));
        String arg1 = "ami", arg2 = "tami", arg3 = "liyami";
        List<PsiExpression> arguments = step.arguments(createTestMethodCallExpression("f", arg1, arg2, arg3));
        assertEquals(3, arguments.size());
        assertEqualsByText(arg1, arguments.get(0));
        assertEqualsByText(arg2, arguments.get(1));
        assertEqualsByText(arg3, arguments.get(2));

    }

    public void testParameters() {
        assertEquals(0, step.parameters((PsiMethod) null).size());
        assertEquals(0, step.parameters((PsiParameterList) null).size());
        String methodCode = "public void foo(int a, int b, int c) { }";

        List<PsiParameter> params = step.parameters(createTestMethodFromString(methodCode));
        assertEquals(3, params.size());
        assertEqualsByText("int a", params.get(0));
        assertEqualsByText("int b", params.get(1));
        assertEqualsByText("int c", params.get(2));

        params = step.parameters(createTestMethodFromString(methodCode).getParameterList());
        assertEquals(3, params.size());
        assertEqualsByText("int a", params.get(0));
        assertEqualsByText("int b", params.get(1));
        assertEqualsByText("int c", params.get(2));
    }

    public void testFields() {
        List<PsiField> fields = step.fields(createTestClassFromString("public class X { private String s; protected final X x; public void foo() { } }"));
        assertEquals(2, fields.size());
        assertEqualsByText("private String s;", fields.get(0));
        assertEqualsByText("protected final X x;", fields.get(1));
    }

    public void testNextSibling() {
        PsiElement sibling = step.nextSibling(createTestCodeBlockFromString("{ int x = 4; \n\r\t\r\r\r\r\r\n     \n \n \r int y = 6;}").getFirstBodyElement());
        assertEquals(PsiDeclarationStatementImpl.class, sibling.getClass());
        assertEqualsByText("int x = 4;", sibling);
        assertEqualsByText("int y = 6;", step.nextSibling(sibling));
    }

    public void testHighestParent() {
        assertEqualsByText("int y",
                step.getHighestParent(step.firstParameter(createTestMethodFromString("void f(int y) { int x = 4; }"))));
    }

    public void testParamterExpression() {
        assertNull(step.firstParameterExpression(null));
        assertNull(step.firstParameterExpression(createTestMethodCallExpression("f")));
        assertNull(step.secondParameterExpression(null));
        assertNull(step.secondParameterExpression(createTestMethodCallExpression("f")));
        assertNull(step.secondParameterExpression(createTestMethodCallExpression("f", "oren")));
    }

    public void testFirstStatement() {
        assertNull(step.firstStatement(null));
        assertNull(step.firstStatement(createTestCodeBlockFromString("{}")));
        assertEqualsByText("int x = 4;", step.firstStatement(createTestCodeBlockFromString("{ int x = 4; }")));
    }

    public void testName() {
        assertNull(step.name(null));
        assertEquals("peko", step.name(createTestFieldDeclarationFromString("private int peko = 0;")));
    }

    public void testStatements() {
        assertEquals(0, step.statements(null).size());
        List<PsiStatement> statements = step.statements(createTestCodeBlockFromString("{ print(oren); int eli = biham; }"));
        assertEquals(2, statements.size());
        assertEqualsByText("print(oren);", statements.get(0));
        assertEqualsByText("int eli = biham;", statements.get(1));
    }

    public void testExpression() {
        assertNull(step.expression((PsiExpressionStatement) null));
        assertEqualsByText("x++", step.expression((PsiExpressionStatement) createTestStatementFromString("x++;")));
    }

    public void testReturnType() {
        assertNull(step.returnType(null));
        assertEquals(PsiType.BOOLEAN, step.returnType(createTestMethodFromString("boolean isAlive(){ return false; }")));
    }

    public void testConditionExpression() {
        assertNull(step.conditionExpression(null));
        assertEqualsByText("b", step.conditionExpression(createTestConditionalExpression("b", "a", "c")));
    }

    public void testBinaryExpressions() {
        assertNull(step.operator(null));
        PsiBinaryExpression e = createBinaryTestExpression("a", "+", "b");
        assertEquals(PLUS, step.operator(e));
        assertNull(step.leftOperand(null));
        assertEqualsByText("a", step.leftOperand(e));
        assertNull(step.rightOperand(null));
        assertEqualsByText("b", step.rightOperand(e));
        assertNull(step.thenExpression(null));
        assertEqualsByText("t", step.thenExpression(createTestConditionalExpression("b", "t", "e")));
        assertNull(step.elseExpression(null));
        assertEqualsByText("e", step.elseExpression(createTestConditionalExpression("b", "t", "e")));
    }


}
