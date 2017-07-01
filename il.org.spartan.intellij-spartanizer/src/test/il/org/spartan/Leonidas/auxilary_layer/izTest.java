package il.org.spartan.Leonidas.auxilary_layer;


import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.impl.source.tree.java.PsiBinaryExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiConditionalExpressionImpl;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Block;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Expression;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Statement;

import java.util.HashMap;

/**
 * @author michal cohen, Amir Sagiv
 * @since 22-12-2016.
 */
public class izTest extends PsiTypeHelper {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testNull$() throws Exception {
        assert iz.null$(createTestNullExpression());
        assert !iz.null$(null);
    }

    public void testNotNull() throws Exception {
        assert !iz.notNull(createTestNullExpression());
        assert iz.notNull(createTestExpressionFromString("x + 1"));
    }

    public void testStatement() throws Exception {
        assert iz.statement(createTestStatementFromString("return x + 1;"));
        assert !iz.statement(createTestCodeBlockFromString("{ return x + 1; }"));
    }

    public void testBlockStatement() throws Exception {
        assert iz.blockStatement(createTestBlockStatementFromString("{ int x = 5; }"));
        assert !iz.blockStatement(createTestStatementFromString("int x = 5;"));
    }

    public void testBinaryExpression() throws Exception {
        assert iz.binaryExpression((PsiBinaryExpression) createTestExpression("x == y"));
        assert iz.binaryExpression((PsiBinaryExpression) createTestExpression("x != y"));
        assert iz.binaryExpression((PsiBinaryExpression) createTestExpression("x + y"));
        assert iz.binaryExpression((PsiBinaryExpression) createTestExpression("x % y"));
        assert !iz.binaryExpression(createTestExpression("!x"));
    }

    public void testReferenceExpression() throws Exception {
        assert iz.referenceExpression(createTestExpression("x.y"));
        assert iz.referenceExpression(createTestExpression("x.y.z"));
        assert iz.referenceExpression(createTestExpression("x"));
        assert !iz.referenceExpression(createTestExpression("x == false"));
    }

    public void testEqualsOperator() throws Exception {
        assert iz.equalsOperator(((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign().getTokenType());
        assert !iz.equalsOperator(((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign().getTokenType());
        assert !iz.equalsOperator(((PsiBinaryExpression) createTestExpression("x >= y")).getOperationSign().getTokenType());
        assert !iz.equalsOperator(null);
        assert !iz.equalsOperator(
                ((PsiBinaryExpression) createTestExpression("x + y")).getOperationSign().getTokenType());
    }

    public void testNotEqualsOperator() throws Exception {
        assert iz.notEqualsOperator(((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign().getTokenType());
        assert !iz.notEqualsOperator(((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign().getTokenType());
        assert !iz.notEqualsOperator(((PsiBinaryExpression) createTestExpression("x >= y")).getOperationSign().getTokenType());
        assert !iz.notEqualsOperator(null);
        assert !iz.notEqualsOperator(
                ((PsiBinaryExpression) createTestExpression("x + y")).getOperationSign().getTokenType());
    }

    public void testLiteral() throws Exception {
        assert iz.literal(createTestExpression("null"));
        assert iz.literal(createTestExpression("false"));
        assert !iz.literal(createTestExpression("b == false"));
    }

    public void testClassDeclaration() throws Exception {
        assert iz.classDeclaration(createTestClassFromString("", "A", "", "public"));
        assert iz.classDeclaration(createTestClassFromString("", "A", "", "private"));
        assert iz.classDeclaration(createTestInterfaceFromString("", "A", "", "public"));
        assert iz.classDeclaration(createTestClassFromString("", "A", "", "public", "abstract"));
    }

    public void testForStatement() throws Exception {
        assert iz.forStatement(createTestForStatementFromString("for(int i =0 ; i< 10 ; i++){}"));
        assert iz.forStatement(createTestForStatementFromString("for(int i =0 ; i< i+1 ; i--){}"));
        assert !iz.forStatement(createTestStatementFromString("for(int x : array){}"));

    }

    public void testForEachStatement() throws Exception {
        assert iz.forEachStatement(createTestForeachStatementFromString("for(int i : list){}"));
        assert iz.forEachStatement(createTestForeachStatementFromString("for(Object o : new ArrayList<Object>()){}"));
        assert !iz.forEachStatement(createTestForStatementFromString("for(int i = 0 ; i<11 ; i++){}"));
    }

    public void testIfStatement() throws Exception {
        assert iz.ifStatement(createTestIfStatement("x > 2", "break;"));
        assert iz.ifStatement(createTestIfStatement("x == 2", "continue;"));
        assert !iz.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}"));

    }

    public void testImportList() throws Exception {
        PsiElement importList = createTestImportListFromString("import java.util.*;" +
                "import sparta.boom;");
        assert iz.importList(importList);
        assert !iz.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}"));
    }

    public void testJavaToken() throws Exception {
        assert iz.javaToken(((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign());
        assert iz.javaToken(((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign());
        assert !iz.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}"));

    }

    public void testMethodCallExpression() throws Exception {
        assert iz.methodCallExpression((PsiMethodCallExpression) createTestExpression("getX()"));
        assert iz.methodCallExpression((PsiMethodCallExpression) createTestExpression("foo(x,y)"));
        assert iz.methodCallExpression(createTestExpression("list.size()"));
        assert !iz.methodCallExpression(createTestExpression("x+y"));
        assert !iz.methodCallExpression(createTestMethodFromString("foo(int x,double y)"));
    }

    public void testTheSameType() throws Exception {
        PsiDeclarationStatement x = createTestDeclarationStatement("x", "Integer", "5 + 8");
        PsiStatement y = createTestStatementFromString("banana(5*x)");
        assert !iz.theSameType(x, y);
        assert iz.theSameType(y, createTestStatementFromString("apple(x - 4)"));
    }

    public void testBlock() throws Exception {
        assert iz.block(createTestCodeBlockFromString("{ int x = 5; }"));
        assert !iz.block(createTestStatementFromString("int x = 5;"));
    }

    public void testDeclarationStatement() throws Exception {
        assert iz.declarationStatement(createTestDeclarationStatement("x", "Integer", "7"));
        assert iz.declarationStatement(createTestDeclarationStatement("x", "Object", "null"));
        assert !iz.declarationStatement(createTestMethodFromString("foo(int x,double y)"));
    }

    public void testEnumConstant() throws Exception {
        assert iz.enumConstant(createTestEnumFromString("ENUM_VALUE_ONE"));
        assert iz.enumConstant(createTestEnumFromString("ENUM_VALUE_TWO(9)"));
        assert !iz.enumConstant(createTestMethodFromString("foo(int x,double y)"));

    }

    public void testFieldDeclaration() throws Exception {
        assert iz.fieldDeclaration(createTestFieldDeclarationFromString("int x;"));
        assert iz.fieldDeclaration(createTestFieldDeclarationFromString("final int x = 9;"));
        assert iz.fieldDeclaration(createTestFieldDeclarationFromString("public static int x;"));
        assert !iz.fieldDeclaration(createTestMethodFromString("foo(int x,double y)"));
    }

    public void testAbstract$() throws Exception {
        assert iz.abstract$(createTestMethodFromString("public abstract void method(){}"));
        assert iz.abstract$(createTestMethodFromString("abstract public void method(int x);"));
        assert !iz.abstract$(createTestMethodFromString("public void method(){}"));

    }

    public void testStatic$() throws Exception {
        assert iz.static$(createTestMethodFromString("public static void method(){}"));
        assert iz.static$(createTestMethodFromString("static abstract public void method(int x);"));
        assert !iz.static$(createTestMethodFromString("public void method(){}"));
    }

    public void testSingleParameterMethod() throws Exception {
        assert iz.singleParameterMethod(createTestMethodFromString("public static void method(int x){}"));
        assert !iz.singleParameterMethod(createTestMethodFromString("public static void method(int x, int y){}"));
        assert !iz.singleParameterMethod(createTestMethodFromString("public void method(){}"));
    }

    public void testMethod() throws Exception {
        assert iz.method(createTestMethodFromString("public static void method(int x){}"));
        assert iz.method(createTestMethodFromString("static abstract public void method(int x);"));
        assert !iz.method(createTestClassFromString("", "method()", "", "public"));
    }

    public void testVoid$() throws Exception {
        assert iz.void$(createTestMethodFromString("public static void method(int x){}"));
        assert iz.void$(createTestMethodFromString("protected void method(){}"));
        assert !iz.void$(createTestMethodFromString("public static boolean method(){return true;}"));
    }

    public void testPublic$() throws Exception {
        assert iz.public$(createTestMethodFromString("public static void method(int x){}"));
        assert !iz.public$(createTestMethodFromString("protected void method(){}"));
        assert !iz.public$(createTestMethodFromString("private static boolean method(){return true;}"));
        assert !iz.public$(null);
        assert !iz.public$(createTestMethodFromString("void method(){}"));
    }

    public void testMain() throws Exception {
        assert iz.main(createTestMethodFromString("public static void main(String [] args){}"));
        assert !iz.main(createTestMethodFromString("public static void method(String [] args){}"));
        assert !iz.main(createTestMethodFromString("private static void main(String [] args){}"));
        assert !iz.main(createTestMethodFromString("protected static void main(String [] args){}"));
        assert !iz.main(createTestMethodFromString("public static void main(String [] arg){}"));
        assert !iz.main(createTestMethodFromString("public void main(String [] args){}"));
    }

    public void testReturnStatement() throws Exception {
        assert iz.returnStatement(createTestStatementFromString("return x"));
        assert iz.returnStatement(createTestStatementFromString("return x.y"));
        assert iz.returnStatement(createTestStatementFromString("return x == null ? y : x"));
        assert !iz.returnStatement(createTestStatementFromString("int x;"));

    }

    public void testType() throws Exception {
        assert iz.type(createTestTypeElementFromString("int"));
        assert iz.type(createTestTypeElementFromString("Integer"));
        assert iz.type(createTestTypeElementFromString("Object"));
        assert iz.type(createTestTypeElementFromString("List<T>"));
        assert !iz.type(createTestStatementFromString("int x;"));

    }

    public void testExpressionStatement() throws Exception {
        assert iz.expressionStatement(createTestStatementFromString("2+3"));
        assert iz.expressionStatement(createTestStatementFromString("true"));
        assert !iz.expressionStatement(createTestStatementFromString("int x = 2+3"));
    }

    public void testIdentifier() throws Exception {
        assert iz.identifier(createTestIdentifierFromString("x"));
        assert iz.identifier(createTestIdentifierFromString("_"));
        assert iz.identifier(createTestIdentifierFromString("$"));
        assert !iz.identifier(createTestStatementFromString("int x;"));
    }

    public void testConditionalExpression() throws Exception {
        assert iz.conditionalExpression(createTestConditionalExpression("x == null", "x = true", "null"));
        assert iz.conditionalExpression(createTestConditionalExpression("x != null", "x = true", null));
        assert !iz.identifier(createTestStatementFromString("int x;"));
    }

    public void testNullExpression() throws Exception {
        PsiElement e = createDummyFile("banana.java", "class A{ int foo(int x) { return x > 0 ? null : x; } }").getNode().getPsi();
        final Wrapper<PsiConditionalExpression> w = new Wrapper<>();
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitConditionalExpression(PsiConditionalExpression x) {
                super.visitConditionalExpression(x);
                w.inner = x;
            }
        });
        assert (iz.conditionalExpression(w.inner));
        assert (iz.nullExpression(w.inner.getThenExpression()));
    }

    public void testExpression() throws Exception {
        assert iz.expression(createTestExpression("x == y"));
        assert iz.expression(createTestExpression("3 >= y"));
        assert !iz.expression(createTestStatementFromString("int x;"));
    }

    public void testWhitespace() throws Exception {
        assert iz.whiteSpace(new PsiWhiteSpaceImpl(""));
        assert !iz.whiteSpace(createTestStatementFromString("int x;"));
    }

    public void testOfType() throws Exception {
        assert iz.ofType(new PsiWhiteSpaceImpl(""), PsiWhiteSpaceImpl.class);
        assert iz.ofType(createTestExpression("x == y"), PsiBinaryExpressionImpl.class);
        assert iz.ofType(createTestConditionalExpression("x == null", "x = true", "null"),
                PsiConditionalExpressionImpl.class);
    }

    public void testDocumentedElement() throws Exception {
        assert iz.documentedElement(createTestClassFromString("/**\n * This is a main class JavaDoc\n */\n", "Main", "", "public"));
        assert iz.documentedElement(createTestClassFromString("", "Main", "", "public"));
        assert iz.documentedElement(createTestMethodFromString("public int getX(){return x;}"));
        assert !iz.documentedElement(createTestMethodCallExpression("foo", "a"));
    }

    public void testJavaDoc() throws Exception {
        assert iz.javadoc(createTestDocCommentFromString("/**\n * This is a main class JavaDoc\n */\n"));
        assert !iz.javadoc(createTestCommentFromString("//comment"));
        assert !iz.javadoc(createTestCommentFromString("/*comment*/"));
    }

    public void testGeneric() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        assert iz.generic(new Block(psiElement));

        psiElement = createTestStatementFromString("int x=0;");
        assert iz.generic(new Statement(psiElement));

        psiElement = createTestExpressionFromString("x+y");
        PsiType psiType = createTestType("Integer");
        assert iz.generic(new Expression(psiElement, psiType));

        assert !iz.generic(Encapsulator.buildTreeFromPsi(psiElement));
    }

    public void testGenericExpression() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        assert !iz.genericExpression(new Block(psiElement));

        psiElement = createTestExpressionFromString("x+y");
        assert iz.genericExpression(new Expression(psiElement, createTestType("Integer")));
    }

    public void testGenericStatement() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        assert !iz.genericStatement(new Block(psiElement));

        psiElement = createTestStatementFromString("int x=0;");
        assert iz.genericStatement(new Statement(psiElement));
    }

    public void testGenericBlock() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        assert iz.genericBlock(new Block(psiElement));

        psiElement = createTestStatementFromString("int x=0;");
        assert !iz.genericBlock(new Statement(psiElement));
    }

    public void testWhileStatement() throws Exception {
        assert iz.whileStatement(createTestWhileStatementFromString("while(true){}"));
        assert !iz.whileStatement(createTestDoWhileStatementFromString("do{}while(true)"));
        assert !iz.whileStatement(createTestForStatementFromString("for(int i = 0 ; i< 5 ; i++){}"));
    }

    public void testSwitchStatement() throws Exception {
        assert iz.switchStatement(createTestSwitchStatementFromString("switch(x){ case 1: break; case 2: break;}"));
        assert iz.switchStatement(createTestSwitchStatementFromString("switch(x){ case 1: break; case 2: break; default: continue;}"));
        assert !iz.switchStatement(createTestForStatementFromString("for(int i = 0 ; i< 5 ; i++){}"));

    }

    public void testLoopStatement() {
        assert iz.loopStatement(createTestForStatementFromString("for(int i =0 ; i< 10 ; i++){}"));
        assert iz.loopStatement(createTestForeachStatementFromString("for(int i : list){}"));
        assert iz.loopStatement(createTestWhileStatementFromString("while(true){}"));
        assert iz.loopStatement(createTestDoWhileStatementFromString("do{}while(true)"));
    }

    public void testTryStatement() {
        assert iz.tryStatement(createTestTryStatement("", "Exception ex", ""));
        assert iz.tryStatement(createTestTryStatement("", "Exception ex", "", ""));
        assert !iz.tryStatement(createTestWhileStatementFromString("while(true){}"));
    }

    public void testEnclosingStatement() throws Exception {
        assert iz.enclosingStatement(createTestSwitchStatementFromString("switch(x){ case 1: break; case 2: break;}"));
        assert iz.enclosingStatement(createTestWhileStatementFromString("while(true){}"));
        assert iz.enclosingStatement(createTestForStatementFromString("for(int i =0 ; i< 10 ; i++){}"));
        assert iz.enclosingStatement(createTestForeachStatementFromString("for(int i : list){}"));
        assert iz.enclosingStatement(createTestIfStatement("x > 2", "break;"));
        assert iz.enclosingStatement(createTestTryStatement("", "Exception ex", ""));
        assert iz.enclosingStatement(createTestTryStatement("", "Exception ex", "", ""));
        assert iz.enclosingStatement(createTestDoWhileStatementFromString("do{}while(true)"));
        assert !iz.enclosingStatement(createTestStatementFromString("int x;"));
    }

    public void testSynchronized() throws Exception {
        assert iz.synchronized¢(createTestMethodFromString("synchronized public int getX(){return x;}"));
        assert !iz.synchronized¢(createTestMethodFromString("public int getX(){return x;}"));
    }

    public void testNative() throws Exception {
        assert iz.native¢(createTestMethodFromString("native public int getX(){return x;}"));
        assert !iz.native¢(createTestMethodFromString("public int getX(){return x;}"));
    }

    public void testFinalMember() throws Exception {
        assert iz.final¢(createTestFieldDeclarationFromString("final private int x"));
        assert !iz.final¢((PsiMember) null);
        assert !iz.final¢(createTestFieldDeclarationFromString("private int x"));
    }

    public void testFinalVariable() throws Exception {
        assert iz.final¢(createTestFieldDeclarationFromString("final private int x"));
        assert !iz.final¢((PsiVariable) null);
        assert !iz.final¢(createTestFieldDeclarationFromString("private int x"));
    }

    public void testDefault() throws Exception {
        assert iz.default¢(createTestMethodFromString("default public int getX(){return x;}"));
        assert !iz.default¢(createTestMethodFromString("public int getX(){return x;}"));
    }

    public void testAnnotation() throws Exception {
        assert iz.annotation(createTestAnnotationFromString("@annotation"));
        assert !iz.annotation(createTestMethodFromString("default public int getX(){@Wrong return x;}"));
    }

    public void testArrayInitializer() throws Exception {
        assert iz.arrayInitializer(createTestExpression("{0,1,2}"));
        assert iz.arrayInitializer(createTestExpression("{0}"));
        assert iz.arrayInitializer(createTestExpression("{}"));
        assert !iz.arrayInitializer(createTestExpression("0"));
    }

    public void testAssignmentExpression() throws Exception {
        assert iz.assignment(createTestExpression("x=5"));
        assert iz.assignment(createTestExpression("x=++y"));
        assert !iz.assignment(createTestExpression("x>=5"));
    }

    public void testBreakStatement() throws Exception {
        assert iz.breakStatement(createTestStatementFromString("break;"));
        assert !iz.breakStatement(createTestStatementFromString("continue;"));
        assert !iz.breakStatement(createTestStatementFromString("i++;"));
    }

    public void testCastExpression() throws Exception {
        assert iz.castExpression(createTestExpression("(double)x"));
        assert iz.castExpression(createTestExpression("(Object)x"));
        assert !iz.castExpression(createTestExpression("(2 * x) * x"));
    }

    public void testClassInstanceCreation() throws Exception {
        assert iz.classInstanceCreation(createTestExpression("new ArrayList()"));
    }

    public void testBooleanLiteral() throws Exception {
        assert iz.booleanLiteral(createTestExpression("true"));
        assert iz.booleanLiteral(createTestExpression("false"));
        assert !iz.booleanLiteral(createTestExpression("null"));
    }

    public void testBooleanType() throws Exception {
        assert iz.booleanType(createTestTypeFromString("boolean"));
        assert !iz.booleanType(null);
        assert !iz.booleanType(createTestTypeFromString("int"));
    }

    public void testConditionalAnd() throws Exception {
        assert iz.conditionalAnd((PsiBinaryExpression) createTestExpression("x && y"));
        assert !iz.conditionalAnd((PsiBinaryExpression) createTestExpression("x || y"));
    }

    public void testConditionalOr() throws Exception {
        assert iz.conditionalOr((PsiBinaryExpression) createTestExpression("x || y"));
        assert !iz.conditionalOr((PsiBinaryExpression) createTestExpression("x && y"));
    }

    public void testEmptyStatement() throws Exception {
        assert iz.emptyStatement(createTestStatementFromString(";"));
        assert !iz.emptyStatement(createTestStatementFromString("int x;"));
    }

    public void testEnumDeclaration() throws Exception {
        assert iz.enumDeclaration(createEnumClassFromString("Day"));
        assert !iz.enumDeclaration(null);
        assert !iz.enumDeclaration(createTestClassFromString("", "Day", "", "public"));
    }

    public void testInterface() throws Exception {
        assert iz.interface¢(createInterfaceClassFromString("A"));
        assert !iz.interface¢(null);
        assert !iz.interface¢(createEnumClassFromString("A"));
    }

    public void testConforms() throws Exception {
        Encapsulator e1 = Encapsulator.buildTreeFromPsi(createTestExpression("1+5")),
                e2 = Encapsulator.buildTreeFromPsi(createTestExpression("1 > 5")),
                e3 = Encapsulator.buildTreeFromPsi(createTestStatementFromString("return x")),
                e4 = Encapsulator.buildTreeFromPsi(createTestStatementFromString("return y"));

        assert iz.conforms(e1, e2, new HashMap<>()).matches();
        assert iz.conforms(e3, e4, new HashMap<>()).matches();
    }

    public void testArithmetic() {
        assert iz.arithmetic(createTestExpression("3"));
        assert iz.arithmetic(createTestExpression("3.0"));
        assert iz.arithmetic(createTestExpression("3 + 5"));
        assert iz.arithmetic(createTestExpression("3 + 5 * 8"));

        assert !iz.arithmetic(createTestExpression("true"));
        assert !iz.arithmetic(createTestExpression("x && y"));
        assert !iz.arithmetic(createTestExpression("3 + i"));
    }

    public void testThisExpression() {
        assert iz.thisExpression(createTestExpression("this"));
        assert !iz.thisExpression(createTestExpression("true"));
        assert !iz.thisExpression(null);
    }


    public void testExpressionList() {
        assert iz.thisExpression(createTestExpression("this"));
        assert !iz.thisExpression(createTestExpression("true"));
        assert !iz.thisExpression(null);
    }

    public void testReferenceParameterList() {
        assert iz.thisExpression(createTestExpression("this"));
        assert !iz.thisExpression(createTestExpression("true"));
        assert !iz.thisExpression(null);
    }
}