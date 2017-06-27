package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiStatement;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Amir Sagiv
 * @since 13-01-2017.
 */
public class azTest extends PsiTypeHelper {

    public void testAzStatement() throws Exception{
        assert az.statement(createTestStatementFromString("int x")) != null;
        assert az.statement(createTestStatementFromString("return true;")) != null;
        assertNull(az.statement(createTestExpressionFromString("x== null ? null : x")));
    }

    public void testAzCodeBlock() throws Exception{
        assert az.block(createTestCodeBlockFromString("{ int x = 5; }")) != null;
        assertNull(az.block(createTestStatementFromString("int x = 5;")));
    }

    public void testAzDeclarationStatement() throws Exception{
        assert az.declarationStatement(createTestDeclarationStatement("x", "Integer", "7")) != null;
        assert az.declarationStatement(createTestDeclarationStatement("x", "Object", "null")) != null;
        assertNull(az.declarationStatement(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzEnumConstant() throws Exception{
        assert az.enumConstant(createTestEnumFromString("ENUM_VALUE_ONE")) != null;
        assert az.enumConstant(createTestEnumFromString("ENUM_VALUE_TWO(9)")) != null;
        assertNull(az.enumConstant(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzFieldDeclaration() throws Exception{
        assert az.fieldDeclaration(createTestFieldDeclarationFromString("int x;")) != null;
        assert az.fieldDeclaration(createTestFieldDeclarationFromString("final int x = 9;")) != null;
        assert az.fieldDeclaration(createTestFieldDeclarationFromString("public static int x;")) != null;
        assertNull(az.fieldDeclaration(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzExpressionStatement() throws Exception {
        assert az.expressionStatement(createTestStatementFromString("2+3")) != null;
        assert az.expressionStatement(createTestStatementFromString("true")) != null;
        assertNull(az.expressionStatement(createTestStatementFromString("int x = 2+3")));
    }

    public void testAzMethodCallExpression() throws Exception{
        assert az.methodCallExpression(createTestExpression("getX()")) != null;
        assert az.methodCallExpression(createTestExpression("foo(x,y)")) != null;
        assert az.methodCallExpression(createTestExpression("list.size()")) != null;
        assertNull(az.methodCallExpression(createTestExpression("x+y")));
        assertNull(az.methodCallExpression(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzIdentifier() throws Exception {
        assert az.identifier(createTestIdentifierFromString("x")) != null;
        assert az.identifier(createTestIdentifierFromString("_")) != null;
        assert az.identifier(createTestIdentifierFromString("$")) != null;
        assertNull(az.identifier(createTestStatementFromString("int x;")));
    }

    public void testAzConditionalExpression() throws Exception {
        assert az.conditionalExpression(createTestConditionalExpression("x == null", "x = true", "null")) != null;
        assert az.conditionalExpression(createTestConditionalExpression("x != null", "x = true", null)) != null;
        assertNull(az.conditionalExpression(createTestStatementFromString("int x;")));
    }

    public void testAzBinaryExpression() throws Exception {
        assert az.binaryExpression(createTestExpression("x == y")) != null;
        assert az.binaryExpression(createTestExpression("x != y")) != null;
        assert az.binaryExpression(createTestExpression("x + y")) != null;
        assert az.binaryExpression(createTestExpression("x % y")) != null;
        assertNull(az.binaryExpression(createTestExpression("!x")));
    }

    public void testAzReferenceExpression() throws Exception {
        assert az.referenceExpression(createTestExpression("x.y")) != null;
        assert az.referenceExpression(createTestExpression("x.y.z")) != null;
        assert az.referenceExpression(createTestExpression("x")) != null;
        assertNull(az.referenceExpression(createTestExpression("x == false")));
    }

    public void testAzLiteral() throws Exception {
        assert az.literal(createTestExpression("null")) != null;
        assert az.literal(createTestExpression("false")) != null;
        assertNull(az.literal(createTestExpression("b == false")));
    }

    public void testAzClassDeclaration() throws Exception {
        assert az.classDeclaration(createTestClassFromString("", "A", "", "public")) != null;
        assert az.classDeclaration(createTestClassFromString("", "A", "", "private")) != null;
        assert az.classDeclaration(createTestInterfaceFromString("", "A", "", "public")) != null;
        assert az.classDeclaration(createTestClassFromString("", "A", "", "public", "abstract")) != null;
    }

    public void testAzForEachStatement() throws Exception {
        assert az.forEachStatement(createTestForeachStatementFromString("for(int i : list){}")) != null;
        assert az.forEachStatement(createTestForeachStatementFromString("for(Object o : new ArrayList<Object>()){}")) != null;
        assertNull(az.forEachStatement(createTestForStatementFromString("for(int i = 0 ; i<11 ; i++){}")));
    }

    public void testIfStatement() throws Exception {
        assert az.ifStatement(createTestIfStatement("x > 2", "break;")) != null;
        assert az.ifStatement(createTestIfStatement("x == 2", "continue;")) != null;
        assertNull(az.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));

    }

    public void testAzReturnStatement() throws Exception {
        assert az.returnStatement(createTestStatementFromString("return x")) != null;
        assert az.returnStatement(createTestStatementFromString("return x.y")) != null;
        assert az.returnStatement(createTestStatementFromString("return x == null ? y : x")) != null;
        PsiStatement s = createTestStatementFromString("int x;");
        assertNull(az.returnStatement(s));
        assertNull(az.returnStatement(s));
    }

    public void testAzImportList() throws Exception {
        PsiElement importList = createTestImportListFromString("import java.util.*;"+
                "import sparta.boom;");
        assert az.importList(importList) != null;
        assertNull(az.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));
    }

    public void testAzJavaToken() throws Exception {
        assert az.javaToken(((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign()) != null;
        assert az.javaToken(((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign()) != null;
        assertNull(az.javaToken(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));
    }

    public void testAzMethod() throws Exception{
        assert az.method(createTestMethodFromString("public static void method(int x){}")) != null;
        assert az.method(createTestMethodFromString("static abstract public void method(int x);")) != null;
        assertNull(az.method(createTestClassFromString("", "method()", "", "public")));
    }

    public void testAzBlockStatement() throws Exception {
        assert az.blockStatement(createTestBlockStatementFromString("{ int x = 5; }")) != null;
        assertNull(az.blockStatement(createTestStatementFromString("int x = 5;")));
    }

}