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
        assertNull(az.statement(null));
    }

    public void testAzCodeBlock() throws Exception{
        assert az.block(createTestCodeBlockFromString("{ int x = 5; }")) != null;
        assertNull(az.block(createTestStatementFromString("int x = 5;")));
        assertNull(az.block(null));
    }

    public void testAzDeclarationStatement() throws Exception{
        assert az.declarationStatement(createTestDeclarationStatement("x", "Integer", "7")) != null;
        assert az.declarationStatement(createTestDeclarationStatement("x", "Object", "null")) != null;
        assertNull(az.declarationStatement(createTestMethodFromString("foo(int x,double y)")));
        assertNull(az.declarationStatement(null));
    }

    public void testAzEnumConstant() throws Exception{
        assert az.enumConstant(createTestEnumFromString("ENUM_VALUE_ONE")) != null;
        assert az.enumConstant(createTestEnumFromString("ENUM_VALUE_TWO(9)")) != null;
        assertNull(az.enumConstant(createTestMethodFromString("foo(int x,double y)")));
        assertNull(az.enumConstant(null));
    }

    public void testAzFieldDeclaration() throws Exception{
        assert az.fieldDeclaration(createTestFieldDeclarationFromString("int x;")) != null;
        assert az.fieldDeclaration(createTestFieldDeclarationFromString("final int x = 9;")) != null;
        assert az.fieldDeclaration(createTestFieldDeclarationFromString("public static int x;")) != null;
        assertNull(az.fieldDeclaration(createTestMethodFromString("foo(int x,double y)")));
        assertNull(az.fieldDeclaration(null));
    }

    public void testAzExpressionStatement() throws Exception {
        assert az.expressionStatement(createTestStatementFromString("2+3")) != null;
        assert az.expressionStatement(createTestStatementFromString("true")) != null;
        assertNull(az.expressionStatement(createTestStatementFromString("int x = 2+3")));
        assertNull(az.expressionStatement(null));
    }

    public void testAzMethodCallExpression() throws Exception{
        assert az.methodCallExpression(createTestExpression("getX()")) != null;
        assert az.methodCallExpression(createTestExpression("foo(x,y)")) != null;
        assert az.methodCallExpression(createTestExpression("list.size()")) != null;
        assertNull(az.methodCallExpression(createTestExpression("x+y")));
        assertNull(az.methodCallExpression(createTestMethodFromString("foo(int x,double y)")));
        assertNull(az.methodCallExpression(null));
    }

    public void testAzIdentifier() throws Exception {
        assert az.identifier(createTestIdentifierFromString("x")) != null;
        assert az.identifier(createTestIdentifierFromString("_")) != null;
        assert az.identifier(createTestIdentifierFromString("$")) != null;
        assertNull(az.identifier(createTestStatementFromString("int x;")));
        assertNull(az.identifier(null));
    }

    public void testAzConditionalExpression() throws Exception {
        assert az.conditionalExpression(createTestConditionalExpression("x == null", "x = true", "null")) != null;
        assert az.conditionalExpression(createTestConditionalExpression("x != null", "x = true", null)) != null;
        assertNull(az.conditionalExpression(createTestStatementFromString("int x;")));
        assertNull(az.conditionalExpression(null));
    }

    public void testAzBinaryExpression() throws Exception {
        assert az.binaryExpression(createTestExpression("x == y")) != null;
        assert az.binaryExpression(createTestExpression("x != y")) != null;
        assert az.binaryExpression(createTestExpression("x + y")) != null;
        assert az.binaryExpression(createTestExpression("x % y")) != null;
        assertNull(az.binaryExpression(createTestExpression("!x")));
        assertNull(az.binaryExpression(null));
    }

    public void testAzReferenceExpression() throws Exception {
        assert az.referenceExpression(createTestExpression("x.y")) != null;
        assert az.referenceExpression(createTestExpression("x.y.z")) != null;
        assert az.referenceExpression(createTestExpression("x")) != null;
        assertNull(az.referenceExpression(createTestExpression("x == false")));
        assertNull(az.referenceExpression(null));
    }

    public void testAzLiteral() throws Exception {
        assert az.literal(createTestExpression("null")) != null;
        assert az.literal(createTestExpression("false")) != null;
        assertNull(az.literal(createTestExpression("b == false")));
        assertNull(az.literal(null));
    }

    public void testAzClassDeclaration() throws Exception {
        assert az.classDeclaration(createTestClassFromString("", "A", "", "public")) != null;
        assert az.classDeclaration(createTestClassFromString("", "A", "", "private")) != null;
        assert az.classDeclaration(createTestInterfaceFromString("", "A", "", "public")) != null;
        assert az.classDeclaration(createTestClassFromString("", "A", "", "public", "abstract")) != null;
        assertNull(az.classDeclaration(null));
    }

    public void testAzForEachStatement() throws Exception {
        assert az.forEachStatement(createTestForeachStatementFromString("for(int i : list){}")) != null;
        assert az.forEachStatement(createTestForeachStatementFromString("for(Object o : new ArrayList<Object>()){}")) != null;
        assertNull(az.forEachStatement(createTestForStatementFromString("for(int i = 0 ; i<11 ; i++){}")));
        assertNull(az.forEachStatement(null));
    }

    public void testIfStatement() throws Exception {
        assert az.ifStatement(createTestIfStatement("x > 2", "break;")) != null;
        assert az.ifStatement(createTestIfStatement("x == 2", "continue;")) != null;
        assertNull(az.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));
        assertNull(az.ifStatement(null));

    }

    public void testAzReturnStatement() throws Exception {
        assert az.returnStatement(createTestStatementFromString("return x")) != null;
        assert az.returnStatement(createTestStatementFromString("return x.y")) != null;
        assert az.returnStatement(createTestStatementFromString("return x == null ? y : x")) != null;
        PsiStatement s = createTestStatementFromString("int x;");
        assertNull(az.returnStatement(s));
        assertNull(az.returnStatement(s));
        assertNull(az.returnStatement(null));
    }

    public void testAzImportList() throws Exception {
        PsiElement importList = createTestImportListFromString("import java.util.*;"+
                "import sparta.boom;");
        assert az.importList(importList) != null;
        assertNull(az.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));
        assertNull(az.importList(null));
    }

    public void testAzJavaToken() throws Exception {
        assert az.javaToken(((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign()) != null;
        assert az.javaToken(((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign()) != null;
        assertNull(az.javaToken(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));
        assertNull(az.javaToken(null));
    }

    public void testAzMethod() throws Exception{
        assert az.method(createTestMethodFromString("public static void method(int x){}")) != null;
        assert az.method(createTestMethodFromString("static abstract public void method(int x);")) != null;
        assertNull(az.method(createTestClassFromString("", "method()", "", "public")));
        assertNull(az.method(null));
    }

    public void testAzBlockStatement() throws Exception {
        assert az.blockStatement(createTestBlockStatementFromString("{ int x = 5; }")) != null;
        assertNull(az.blockStatement(createTestStatementFromString("int x = 5;")));
        assertNull(az.blockStatement(null));
    }

    public void testAzExpression() throws Exception {
        assert az.expression(createTestExpressionFromString("true == false")) != null;
        assertNull(az.expression(createTestStatementFromString("int x = 5;")));
        assertNull(az.expression(null));
    }

    public void testAzModifierListOwner() throws Exception {
        assert az.modifierListOwner(createTestMethodFromString("public static goo(){}")) != null;
        assertNull(az.modifierListOwner(createTestStatementFromString("int x = 5;")));
        assertNull(az.modifierListOwner(null));
    }

    public void testAzType() throws Exception {
        assert az.type(createTestTypeElementFromString("int")) != null;
        assertNull(az.type(createTestStatementFromString("int x = 5;")));
        assertNull(az.type(null));
    }

    public void testAzComment() throws Exception {
        assert az.comment(createTestCommentFromString("//comment")) != null;
        assertNull(az.comment(createTestStatementFromString("int x = 5;")));
        assertNull(az.comment(null));
    }

    public void testAzFile() throws Exception {
        assert az.psiFile(createTestFileFromString("public class A{}")) != null;
        assertNull(az.psiFile(null));
    }

    public void testAzNewExpression() throws Exception {
        assert az.newExpression(createTestExpressionFromString("new A()")) != null;
        assertNull(az.newExpression(createTestStatementFromString("int x = 5;")));
        assertNull(az.newExpression(null));
    }

}