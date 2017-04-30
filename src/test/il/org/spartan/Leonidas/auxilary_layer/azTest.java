package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiStatement;
import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author Amir Sagiv
 * @since 13-01-2017.
 */
public class azTest extends PsiTypeHelper {

    public void testAzStatement() throws Exception{
        PsiElement e1 = createTestStatementFromString("int x");
        assert az.statement(e1) != null;
        PsiElement e2 = createTestStatementFromString("return true;");
        assert az.statement(e2) != null;
        assertNull(az.statement(createTestExpressionFromString("x== null ? null : x")));
    }

    public void testAzCodeBlock() throws Exception{
        PsiElement e1 = createTestCodeBlockFromString("{ int x = 5; }");
        assert az.block(e1) != null;
        assertNull(az.block(createTestStatementFromString("int x = 5;")));
    }

    public void testAzDeclarationStatement() throws Exception{
        PsiElement integerDec = createTestDeclarationStatement("x", "Integer" , "7");
        assert az.declarationStatement(integerDec) != null;
        PsiElement objDec = createTestDeclarationStatement("x", "Object" , "null");
        assert az.declarationStatement(objDec) != null;
        assertNull(az.declarationStatement(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzEnumConstant() throws Exception{
        PsiElement ec1 = createTestEnumFromString("ENUM_VALUE_ONE");
        assert az.enumConstant(ec1) != null;
        PsiElement ec2 = createTestEnumFromString("ENUM_VALUE_TWO(9)");
        assert az.enumConstant(ec2) != null;
        assertNull(az.enumConstant(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzFieldDeclaration() throws Exception{
        PsiElement f1 = createTestFieldDeclarationFromString("int x;");
        assert az.fieldDeclaration(f1) != null;
        PsiElement f2 = createTestFieldDeclarationFromString("final int x = 9;");
        assert az.fieldDeclaration(f2) != null;
        PsiElement f3 = createTestFieldDeclarationFromString("public static int x;");
        assert az.fieldDeclaration(f3) != null;
        assertNull(az.fieldDeclaration(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzExpressionStatement() throws Exception {
        PsiElement s1 = createTestStatementFromString("2+3");
        assert az.expressionStatement(s1) != null;
        PsiElement s2 = createTestStatementFromString("true");
        assert az.expressionStatement(s2) != null;
        assertNull(az.expressionStatement(createTestStatementFromString("int x = 2+3")));
    }

    public void testAzMethodCallExpression() throws Exception{
        PsiElement getterCall = createTestExpression("getX()");
        assert az.methodCallExpression(getterCall) != null;
        PsiElement foo = createTestExpression("foo(x,y)");
        assert az.methodCallExpression(foo) != null;
        PsiElement listSize= createTestExpression("list.size()");
        assert az.methodCallExpression(listSize) != null;
        PsiElement ifExp= createTestExpression("x+y");
        assertNull(az.methodCallExpression(ifExp));
        assertNull(az.methodCallExpression(createTestMethodFromString("foo(int x,double y)")));
    }

    public void testAzIdentifier() throws Exception {
        PsiElement id1 = createTestIdentifierFromString("x");
        assert az.identifier(id1) != null;
        PsiElement id2 = createTestIdentifierFromString("_");
        assert az.identifier(id2) != null;
        PsiElement id3 = createTestIdentifierFromString("$");
        assert az.identifier(id3) != null;
        assertNull(az.identifier(createTestStatementFromString("int x;")));
    }

    public void testAzConditionalExpression() throws Exception {
        PsiElement c1= createTestConditionalExpression("x == null" , "x = true" , "null");
        assert az.conditionalExpression(c1) != null;
        PsiElement c2= createTestConditionalExpression("x != null" , "x = true" ,null);
        assert az.conditionalExpression(c2) != null;
        assertNull(az.conditionalExpression(createTestStatementFromString("int x;")));
    }

    public void testAzBinaryExpression() throws Exception {
        PsiElement eq = createTestExpression("x == y");
        assert az.binaryExpression(eq) != null;
        PsiElement neq = createTestExpression("x != y");
        assert az.binaryExpression(neq) != null;
        PsiElement bPlus = createTestExpression("x + y");
        assert az.binaryExpression(bPlus) != null;
        PsiElement bMod = createTestExpression("x % y");
        assert az.binaryExpression(bMod) != null;
        assertNull(az.binaryExpression(createTestExpression("!x")));
    }

    public void testAzReferenceExpression() throws Exception {
        PsiExpression rf1 = createTestExpression("x.y");
        assert az.referenceExpression(rf1) != null;
        PsiExpression rf2 = createTestExpression("x.y.z");
        assert az.referenceExpression(rf2) != null;
        PsiExpression rf3 = createTestExpression("x");
        assert az.referenceExpression(rf3) != null;
        assertNull(az.referenceExpression(createTestExpression("x == false")));
    }

    public void testAzLiteral() throws Exception {
        PsiElement l1 = createTestExpression("null");
        assert az.literal(l1) != null;
        PsiElement l2 = createTestExpression("false");
        assert az.literal(l2) != null;
        assertNull(az.literal(createTestExpression("b == false")));
    }

    public void testAzClassDeclaration() throws Exception {
        PsiElement c1 = createTestClassFromString("", "A", "", "public");
        assert az.classDeclaration(c1) != null;
        PsiElement c2 = createTestClassFromString("", "A", "", "private");
        assert az.classDeclaration(c2) != null;
        PsiElement i1 = createTestInterfaceFromString("", "A", "", "public");
        assert az.classDeclaration(i1) != null;
        assert az.classDeclaration(createTestClassFromString("", "A", "", "public", "abstract")) != null;
    }

    public void testAzForEachStatement() throws Exception {
        PsiElement e1= createTestForeachStatementFromString("for(int i : list){}");
        assert az.forEachStatement(e1) != null;
        PsiElement e2= createTestForeachStatementFromString("for(Object o : new ArrayList<Object>()){}");
        assert az.forEachStatement(e2) != null;
        assertNull(az.forEachStatement(createTestForStatementFromString("for(int i = 0 ; i<11 ; i++){}")));
    }

    public void testIfStatement() throws Exception {
        PsiElement if1 = createTestIfStatement("x > 2", "break;");
        assert az.ifStatement(if1) != null;
        PsiElement if2 = createTestIfStatement("x == 2", "continue;");
        assert az.ifStatement(if2) != null;
        assertNull(az.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));

    }

    public void testAzReturnStatement() throws Exception {
        PsiStatement rt1 = createTestStatementFromString("return x");
        assert az.returnStatement(rt1) != null;
        PsiStatement rt2 = createTestStatementFromString("return x.y");
        assert az.returnStatement(rt2) != null;
        PsiStatement rt3 = createTestStatementFromString("return x == null ? y : x");
        assert az.returnStatement(rt3) != null;
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
        PsiElement t1 = ((PsiBinaryExpression)createTestExpression("x == y")).getOperationSign();
        assert az.javaToken(t1) != null;
        PsiElement t2 = ((PsiBinaryExpression)createTestExpression("x != y")).getOperationSign();
        assert az.javaToken(t2) != null;
        assertNull(az.javaToken(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}")));
    }

    public void testAzMethod() throws Exception{
        PsiElement e1 = createTestMethodFromString("public static void method(int x){}");
        assert az.method(e1) != null;
        PsiElement e2 = createTestMethodFromString("static abstract public void method(int x);");
        assert az.method(e2) != null;
        assertNull(az.method(createTestClassFromString("", "method()", "", "public")));
    }

    public void testAzBlockStatement() throws Exception {
        PsiElement b = createTestBlockStatementFromString("{ int x = 5; }");
        assert az.blockStatement(b) != null;
        assertNull(az.blockStatement(createTestStatementFromString("int x = 5;")));
    }

}