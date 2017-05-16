package il.org.spartan.Leonidas.auxilary_layer;


import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.impl.source.tree.java.PsiBinaryExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiConditionalExpressionImpl;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsiBlock;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsiExpression;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsiStatement;

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
    }

    public void testNotNull() throws Exception {
        PsiLiteralExpression x = createTestNullExpression();
        assert !iz.notNull(x);
        assert iz.notNull(createTestExpressionFromString("x + 1"));
    }

    public void testStatement() throws Exception {
        PsiStatement s = createTestStatementFromString("return x + 1;");
        assert iz.statement(s);
        assert !iz.statement(createTestCodeBlockFromString("{ return x + 1; }"));
    }

    public void testBlockStatement() throws Exception {
        PsiElement b = createTestBlockStatementFromString("{ int x = 5; }");
        assert iz.blockStatement(b);
        assert !iz.blockStatement(createTestStatementFromString("int x = 5;"));
    }

    public void testBinaryExpression() throws Exception {
        PsiBinaryExpression eq = (PsiBinaryExpression) createTestExpression("x == y");
        assert iz.binaryExpression(eq);
        PsiBinaryExpression neq = (PsiBinaryExpression) createTestExpression("x != y");
        assert iz.binaryExpression(neq);
        PsiBinaryExpression bPlus = (PsiBinaryExpression) createTestExpression("x + y");
        assert iz.binaryExpression(bPlus);
        PsiBinaryExpression bMod = (PsiBinaryExpression) createTestExpression("x % y");
        assert iz.binaryExpression(bMod);
        assert !iz.binaryExpression(createTestExpression("!x"));
    }

    public void testReferenceExpression() throws Exception {
        PsiExpression rf1 = createTestExpression("x.y");
        assert iz.referenceExpression(rf1);
        PsiExpression rf2 = createTestExpression("x.y.z");
        assert iz.referenceExpression(rf2);
        PsiExpression rf3 = createTestExpression("x");
        assert iz.referenceExpression(rf3);
        assert !iz.referenceExpression(createTestExpression("x == false"));
    }

    public void testEqualsOperator() throws Exception {
        PsiJavaToken t1 = ((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign();
        assert iz.equalsOperator(t1.getTokenType());
        PsiJavaToken t2 = ((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign();
        assert !iz.equalsOperator(t2.getTokenType());
        PsiJavaToken t3 = ((PsiBinaryExpression) createTestExpression("x >= y")).getOperationSign();
        assert !iz.equalsOperator(t3.getTokenType());
        assert !iz.equalsOperator(
				((PsiBinaryExpression) createTestExpression("x + y")).getOperationSign().getTokenType());
    }

    public void testNotEqualsOperator() throws Exception {
        PsiJavaToken t1 = ((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign();
        assert iz.notEqualsOperator(t1.getTokenType());
        PsiJavaToken t2 = ((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign();
        assert !iz.notEqualsOperator(t2.getTokenType());
        PsiJavaToken t3 = ((PsiBinaryExpression) createTestExpression("x >= y")).getOperationSign();
        assert !iz.notEqualsOperator(t3.getTokenType());
        assert !iz.notEqualsOperator(
				((PsiBinaryExpression) createTestExpression("x + y")).getOperationSign().getTokenType());
    }

    public void testLiteral() throws Exception {
        PsiExpression l1 = createTestExpression("null");
        assert iz.literal(l1);
        PsiExpression l2 = createTestExpression("false");
        assert iz.literal(l2);
        assert !iz.literal(createTestExpression("b == false"));
    }

    public void testClassDeclaration() throws Exception {
        PsiElement c1 = createTestClassFromString("", "A", "", "public");
        assert iz.classDeclaration(c1);
        PsiElement c2 = createTestClassFromString("", "A", "", "private");
        assert iz.classDeclaration(c2);
        PsiElement i1 = createTestInterfaceFromString("", "A", "", "public");
        assert iz.classDeclaration(i1);
        assert iz.classDeclaration(createTestClassFromString("", "A", "", "public", "abstract"));
    }

    public void testForStatement() throws Exception {
        PsiElement e1 = createTestForStatementFromString("for(int i =0 ; i< 10 ; i++){}");
        assert iz.forStatement(e1);
        PsiElement e2 = createTestForStatementFromString("for(int i =0 ; i< i+1 ; i--){}");
        assert iz.forStatement(e2);
        assert !iz.forStatement(createTestStatementFromString("for(int x : array){}"));

    }

    public void testForEachStatement() throws Exception {
        PsiElement e1 = createTestForeachStatementFromString("for(int i : list){}");
        assert iz.forEachStatement(e1);
        PsiElement e2 = createTestForeachStatementFromString("for(Object o : new ArrayList<Object>()){}");
        assert iz.forEachStatement(e2);
        assert !iz.forEachStatement(createTestForStatementFromString("for(int i = 0 ; i<11 ; i++){}"));
    }

    public void testIfStatement() throws Exception {
        PsiElement if1 = createTestIfStatement("x > 2", "break;");
        assert iz.ifStatement(if1);
        PsiElement if2 = createTestIfStatement("x == 2", "continue;");
        assert iz.ifStatement(if2);
        assert !iz.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}"));

    }

    public void testImportList() throws Exception {
        PsiElement importList = createTestImportListFromString("import java.util.*;" +
                "import sparta.boom;");
        assert iz.importList(importList);
        assert !iz.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}"));
    }

    public void testJavaToken() throws Exception {
        PsiJavaToken t1 = ((PsiBinaryExpression) createTestExpression("x == y")).getOperationSign();
        assert iz.javaToken(t1);
        PsiJavaToken t2 = ((PsiBinaryExpression) createTestExpression("x != y")).getOperationSign();
        assert iz.javaToken(t2);
        assert !iz.ifStatement(createTestForStatementFromString("for(int i = 0 ; i < 5 ; i++){}"));

    }

    public void testMethodCallExpression() throws Exception {
        PsiMethodCallExpression getterCall = (PsiMethodCallExpression) createTestExpression("getX()");
        assert iz.methodCallExpression(getterCall);
        PsiMethodCallExpression foo = (PsiMethodCallExpression) createTestExpression("foo(x,y)");
        assert iz.methodCallExpression(foo);
        PsiExpression listSize = createTestExpression("list.size()");
        assert iz.methodCallExpression(listSize);
        PsiExpression ifExp = createTestExpression("x+y");
        assert !iz.methodCallExpression(ifExp);
        assert !iz.methodCallExpression(createTestMethodFromString("foo(int x,double y)"));
    }

    public void testTheSameType() throws Exception {
        PsiDeclarationStatement x = createTestDeclarationStatement("x", "Integer", "5 + 8");
        PsiStatement y = createTestStatementFromString("banana(5*x)");
        assert !iz.theSameType(x, y);
        assert iz.theSameType(y, createTestStatementFromString("apple(x - 4)"));
    }

    public void testBlock() throws Exception {
        PsiCodeBlock b = createTestCodeBlockFromString("{ int x = 5; }");
        assert iz.block(b);
        assert !iz.block(createTestStatementFromString("int x = 5;"));
    }

    public void testDeclarationStatement() throws Exception {
        PsiDeclarationStatement integerDec = createTestDeclarationStatement("x", "Integer", "7");
        assert iz.declarationStatement(integerDec);
        PsiDeclarationStatement objDec = createTestDeclarationStatement("x", "Object", "null");
        assert iz.declarationStatement(objDec);
        assert !iz.declarationStatement(createTestMethodFromString("foo(int x,double y)"));
    }

    public void testEnumConstant() throws Exception {
        PsiEnumConstant ec1 = createTestEnumFromString("ENUM_VALUE_ONE");
        assert iz.enumConstant(ec1);
        PsiEnumConstant ec2 = createTestEnumFromString("ENUM_VALUE_TWO(9)");
        assert iz.enumConstant(ec2);
        assert !iz.enumConstant(createTestMethodFromString("foo(int x,double y)"));

    }

    public void testFieldDeclaration() throws Exception {
        PsiField f1 = createTestFieldDeclarationFromString("int x;");
        assert iz.fieldDeclaration(f1);
        PsiField f2 = createTestFieldDeclarationFromString("final int x = 9;");
        assert iz.fieldDeclaration(f2);
        PsiField f3 = createTestFieldDeclarationFromString("public static int x;");
        assert iz.fieldDeclaration(f3);
        assert !iz.fieldDeclaration(createTestMethodFromString("foo(int x,double y)"));
    }

    public void testAbstract$() throws Exception {
        PsiMethod m1 = createTestMethodFromString("public abstract void method(){}");
        assert iz.abstract$(m1);
        PsiMethod m2 = createTestMethodFromString("abstract public void method(int x);");
        assert iz.abstract$(m2);
        assert !iz.abstract$(createTestMethodFromString("public void method(){}"));

    }

    public void testStatic$() throws Exception {
        PsiMethod m1 = createTestMethodFromString("public static void method(){}");
        assert iz.static$(m1);
        PsiMethod m2 = createTestMethodFromString("static abstract public void method(int x);");
        assert iz.static$(m2);
        assert !iz.static$(createTestMethodFromString("public void method(){}"));
    }

    public void testSingleParameterMethod() throws Exception {
        PsiMethod m1 = createTestMethodFromString("public static void method(int x){}");
        assert iz.singleParameterMethod(m1);
        PsiMethod m2 = createTestMethodFromString("public static void method(int x, int y){}");
        assert !iz.singleParameterMethod(m2);
        assert !iz.singleParameterMethod(createTestMethodFromString("public void method(){}"));
    }

    public void testMethod() throws Exception {
        PsiElement e1 = createTestMethodFromString("public static void method(int x){}");
        assert iz.method(e1);
        PsiElement e2 = createTestMethodFromString("static abstract public void method(int x);");
        assert iz.method(e2);
        assert !iz.method(createTestClassFromString("", "method()", "", "public"));
    }

    public void testVoid$() throws Exception {
        PsiMethod m1 = createTestMethodFromString("public static void method(int x){}");
        assert iz.void$(m1);
        PsiMethod m2 = createTestMethodFromString("protected void method(){}");
        assert iz.void$(m2);
        assert !iz.void$(createTestMethodFromString("public static boolean method(){return true;}"));
    }

    public void testPublic$() throws Exception {
        PsiMethod m1 = createTestMethodFromString("public static void method(int x){}");
        assert iz.public$(m1);
        PsiMethod m2 = createTestMethodFromString("protected void method(){}");
        assert !iz.public$(m2);
        PsiMethod m3 = createTestMethodFromString("private static boolean method(){return true;}");
        assert !iz.public$(m3);
        assert !iz.public$(createTestMethodFromString("void method(){}"));
    }

    public void testMain() throws Exception {
        PsiMethod m1 = createTestMethodFromString("public static void main(String [] args){}");
        assert iz.main(m1);
        PsiMethod m2 = createTestMethodFromString("public static void method(String [] args){}");
        assert !iz.main(m2);
        PsiMethod m3 = createTestMethodFromString("private static void main(String [] args){}");
        assert !iz.main(m3);
        PsiMethod m4 = createTestMethodFromString("protected static void main(String [] args){}");
        assert !iz.main(m4);
        PsiMethod m5 = createTestMethodFromString("public static void main(String [] arg){}");
        assert !iz.main(m5);
        assert !iz.main(createTestMethodFromString("public void main(String [] args){}"));
    }

    public void testReturnStatement() throws Exception {
        PsiStatement rt1 = createTestStatementFromString("return x");
        assert iz.returnStatement(rt1);
        PsiStatement rt2 = createTestStatementFromString("return x.y");
        assert iz.returnStatement(rt2);
        PsiStatement rt3 = createTestStatementFromString("return x == null ? y : x");
        assert iz.returnStatement(rt3);
        assert !iz.returnStatement(createTestStatementFromString("int x;"));

    }

    public void testType() throws Exception {
        PsiElement te1 = createTestTypeElementFromString("int");
        assert iz.type(te1);
        PsiElement te2 = createTestTypeElementFromString("Integer");
        assert iz.type(te2);
        PsiElement te3 = createTestTypeElementFromString("Object");
        assert iz.type(te3);
        PsiElement te4 = createTestTypeElementFromString("List<T>");
        assert iz.type(te4);
        assert !iz.type(createTestStatementFromString("int x;"));

    }

    public void testExpressionStatement() throws Exception {
        PsiStatement s1 = createTestStatementFromString("2+3");
        assert iz.expressionStatement(s1);
        PsiStatement s2 = createTestStatementFromString("true");
        assert iz.expressionStatement(s2);
        assert !iz.expressionStatement(createTestStatementFromString("int x = 2+3"));
    }

    public void testIdentifier() throws Exception {
        PsiIdentifier id1 = createTestIdentifierFromString("x");
        assert iz.identifier(id1);
        PsiIdentifier id2 = createTestIdentifierFromString("_");
        assert iz.identifier(id2);
        PsiIdentifier id3 = createTestIdentifierFromString("$");
        assert iz.identifier(id3);
        assert !iz.identifier(createTestStatementFromString("int x;"));
    }

    public void testConditionalExpression() throws Exception {
        PsiConditionalExpression c1 = createTestConditionalExpression("x == null", "x = true", "null");
        assert iz.conditionalExpression(c1);
        PsiConditionalExpression c2 = createTestConditionalExpression("x != null", "x = true", null);
        assert iz.conditionalExpression(c2);
        assert !iz.identifier(createTestStatementFromString("int x;"));
    }

    public void testNullExpression() throws Exception {
        PsiFile f = createDummyFile("banana.java", "class A{ int foo(int x) { return x > 0 ? null : x; } }");
        PsiElement e = f.getNode().getPsi();
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
        PsiElement e1 = createTestExpression("x == y");
        assert iz.expression(e1);
        PsiElement e2 = createTestExpression("3 >= y");
        assert iz.expression(e2);
        assert !iz.expression(createTestStatementFromString("int x;"));
    }

    public void testWhitespace() throws Exception {
        PsiWhiteSpace ws = new PsiWhiteSpaceImpl("");
        assert iz.whiteSpace(ws);
        assert !iz.whiteSpace(createTestStatementFromString("int x;"));
    }

    public void testOfType() throws Exception {
        PsiWhiteSpace ws = new PsiWhiteSpaceImpl("");
        assert iz.ofType(ws, PsiWhiteSpaceImpl.class);
        PsiElement e1 = createTestExpression("x == y");
        assert iz.ofType(e1, PsiBinaryExpressionImpl.class);
        assert iz.ofType(createTestConditionalExpression("x == null", "x = true", "null"),
				PsiConditionalExpressionImpl.class);
    }

    public void testDocumentedElement() throws Exception {
        PsiElement e = createTestClassFromString("/**\n" +
                " * This is a main class JavaDoc\n" +
                " */\n", "Main", "", "public");
        assert iz.documentedElement(e);
        PsiElement e2 = createTestClassFromString("", "Main", "", "public");
        assert iz.documentedElement(e2);
        PsiMethod m = createTestMethodFromString("public int getX(){return x;}");
        assert iz.documentedElement(m);
        assert !iz.documentedElement(createTestMethodCallExpression("foo", "a"));
    }

    public void testjavaDoc() throws Exception {
        PsiElement e = createTestDocCommentFromString("/**\n" +
                " * This is a main class JavaDoc\n" +
                " */\n");
        assert iz.javadoc(e);
        PsiElement e2 = createTestCommentFromString("//comment");
        assert !iz.javadoc(e2);
        assert !iz.javadoc(createTestCommentFromString("/*comment*/"));
    }

    public void testGeneric() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        GenericPsiBlock genericPsiBlock = new GenericPsiBlock(psiElement);
        assert iz.generic(genericPsiBlock);

        psiElement = createTestStatementFromString("int x=0;");
        GenericPsiStatement genericPsiStatement = new GenericPsiStatement(psiElement);
        assert iz.generic(genericPsiStatement);

        psiElement = createTestExpressionFromString("x+y");
        PsiType psiType = createTestType("Integer");
        GenericPsiExpression genericPsiExpression = new GenericPsiExpression(psiType, psiElement);
        assert iz.generic(genericPsiExpression);

        assert !iz.generic(psiElement);
    }

    public void testGenericExpression() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        GenericPsiBlock genericPsiBlock = new GenericPsiBlock(psiElement);
        assert !iz.genericExpression(genericPsiBlock);

        psiElement = createTestExpressionFromString("x+y");
        assert iz.genericExpression(new GenericPsiExpression(createTestType("Integer"), psiElement));
    }

    public void testGenericStatement() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        GenericPsiBlock genericPsiBlock = new GenericPsiBlock(psiElement);
        assert !iz.genericStatement(genericPsiBlock);

        psiElement = createTestStatementFromString("int x=0;");
        assert iz.genericStatement(new GenericPsiStatement(psiElement));
    }

    public void testGenericBlock() {
        PsiElement psiElement = createTestBlockStatementFromString("{int x=0;}");
        GenericPsiBlock genericPsiBlock = new GenericPsiBlock(psiElement);
        assert iz.genericBlock(genericPsiBlock);

        psiElement = createTestStatementFromString("int x=0;");
        assert !iz.genericBlock(new GenericPsiStatement(psiElement));
    }

    public void testWhileStatement() throws Exception {
        PsiElement e1 = createTestWhileStatementFromString("while(true){}");
        assert iz.whileStatement(e1);
        PsiElement e2 = createTestDoWhileStatementFromString("do{}while(true)");
        assert !iz.whileStatement(e2);
        assert !iz.whileStatement(createTestForStatementFromString("for(int i = 0 ; i< 5 ; i++){}"));
    }

    public void testSwitchStatement() throws Exception {
        PsiElement e1 = createTestSwitchStatementFromString("switch(x){ case 1: break; case 2: break;}");
        assert iz.switchStatement(e1);
        PsiElement e2 = createTestSwitchStatementFromString("switch(x){ case 1: break; case 2: break; default: continue;}");
        assert iz.switchStatement(e2);
        assert !iz.switchStatement(createTestForStatementFromString("for(int i = 0 ; i< 5 ; i++){}"));

    }

    public void testLoopStatement() {
        PsiElement e1 = createTestForStatementFromString("for(int i =0 ; i< 10 ; i++){}");
        assert iz.loopStatement(e1);
        PsiElement e2 = createTestForeachStatementFromString("for(int i : list){}");
        assert iz.loopStatement(e2);
        PsiElement e3 = createTestWhileStatementFromString("while(true){}");
        assert iz.loopStatement(e3);
        assert iz.loopStatement(createTestDoWhileStatementFromString("do{}while(true)"));
    }

    public void testTryStatement() {
        PsiElement e1 = createTestTryStatement("", "Exception ex", "");
        assert iz.tryStatement(e1);
        PsiElement e2 = createTestTryStatement("", "Exception ex", "", "");
        assert iz.tryStatement(e2);
        assert !iz.tryStatement(createTestWhileStatementFromString("while(true){}"));
    }

    public void testEnclosingStatement() throws Exception {
        PsiElement e1 = createTestSwitchStatementFromString("switch(x){ case 1: break; case 2: break;}");
        assert iz.enclosingStatement(e1);
        PsiElement e2 = createTestWhileStatementFromString("while(true){}");
        assert iz.enclosingStatement(e2);
        PsiElement e3 = createTestForStatementFromString("for(int i =0 ; i< 10 ; i++){}");
        assert iz.enclosingStatement(e3);
        PsiElement e4 = createTestForeachStatementFromString("for(int i : list){}");
        assert iz.enclosingStatement(e4);
        PsiElement e5 = createTestIfStatement("x > 2", "break;");
        assert iz.enclosingStatement(e5);
        PsiElement e6 = createTestTryStatement("", "Exception ex", "");
        assert iz.enclosingStatement(e6);
        PsiElement e7 = createTestTryStatement("", "Exception ex", "", "");
        assert iz.enclosingStatement(e7);
        PsiElement e8 = createTestDoWhileStatementFromString("do{}while(true)");
        assert iz.enclosingStatement(e8);
        assert !iz.enclosingStatement(createTestStatementFromString("int x;"));
    }

    public void testSynchronized() throws Exception {
        PsiMethod e1 = createTestMethodFromString("synchronized public int getX(){return x;}");
        assert iz.synchronized¢(e1);
        assert !iz.synchronized¢(createTestMethodFromString("public int getX(){return x;}"));
    }

    public void testNative() throws Exception {
        PsiMethod e1 = createTestMethodFromString("native public int getX(){return x;}");
        assert iz.native¢(e1);
        assert !iz.native¢(createTestMethodFromString("public int getX(){return x;}"));
    }

    public void testFinalMember() throws Exception {
        PsiMember e1 = createTestFieldDeclarationFromString("final private int x");
        assertTrue(iz.final¢(e1));
        PsiMember e2 = createTestFieldDeclarationFromString("private int x");
        assertFalse(iz.final¢(e2));
    }

    public void testFinalVariable() throws Exception {
        PsiVariable e1 = createTestFieldDeclarationFromString("final private int x");
        assertTrue(iz.final¢(e1));
        PsiVariable e2 = createTestFieldDeclarationFromString("private int x");
        assertFalse(iz.final¢(e2));
    }

    public void testDefault() throws Exception {
        PsiMethod e1 = createTestMethodFromString("default public int getX(){return x;}");
        assert iz.default¢(e1);
        assert !iz.default¢(createTestMethodFromString("public int getX(){return x;}"));
    }

    public void testAnnotation() throws Exception {
        PsiElement e1 = createTestAnnotationFromString("@annotation");
        assert iz.annotation(e1);
        assert !iz.annotation(createTestMethodFromString("default public int getX(){@Wrong return x;}"));
    }

    public void testArrayInitializer() throws Exception {
        PsiElement e1 = createTestExpression("{0,1,2}");
        assert iz.arrayInitializer(e1);
        PsiElement e2 = createTestExpression("{0}");
        assert iz.arrayInitializer(e2);
        PsiElement e3 = createTestExpression("{}");
        assert iz.arrayInitializer(e3);
        assert !iz.arrayInitializer(createTestExpression("0"));
    }

    public void testAssignmentExpression() throws Exception {
        PsiElement e1 = createTestExpression("x=5");
        assert iz.assignment(e1);
        PsiElement e2 = createTestExpression("x=++y");
        assert iz.assignment(e2);
        assert !iz.assignment(createTestExpression("x>=5"));
    }

    public void testBreakStatement() throws Exception {
        PsiElement e1 = createTestStatementFromString("break;");
        assert iz.breakStatement(e1);
        PsiElement e2 = createTestStatementFromString("continue;");
        assert !iz.breakStatement(e2);
        assert !iz.breakStatement(createTestStatementFromString("i++;"));
    }

    public void testCastExpression() throws Exception {
        PsiElement e1 = createTestExpression("(double)x");
        assert iz.castExpression(e1);
        PsiElement e2 = createTestExpression("(Object)x");
        assert iz.castExpression(e2);
        assert !iz.castExpression(createTestExpression("(2 * x) * x"));
    }

    public void testClassInstanceCreation() throws Exception {
        assert iz.classInstanceCreation(createTestExpression("new ArrayList()"));
    }

    public void testBooleanLiteral() throws Exception {
        PsiElement e1 = createTestExpression("true");
        assert iz.booleanLiteral(e1);
        PsiElement e2 = createTestExpression("false");
        assert iz.booleanLiteral(e2);
        assert !iz.booleanLiteral(createTestExpression("null"));
    }

    public void testBooleanType() throws Exception {
        PsiType e1 = createTestTypeFromString("boolean");
        assert iz.booleanType(e1);
        assert !iz.booleanType(createTestTypeFromString("int"));
    }

    public void testConditionalAnd() throws Exception {
        PsiBinaryExpression e1 = (PsiBinaryExpression) createTestExpression("x && y");
        assert iz.conditionalAnd(e1);
        assert !iz.conditionalAnd((PsiBinaryExpression) createTestExpression("x || y"));
    }

    public void testConditionalOr() throws Exception {
        PsiBinaryExpression e1 = (PsiBinaryExpression) createTestExpression("x || y");
        assert iz.conditionalOr(e1);
        assert !iz.conditionalOr((PsiBinaryExpression) createTestExpression("x && y"));
    }

    public void testEmptyStatement() throws Exception {
        PsiElement e1 = createTestStatementFromString(";");
        assert iz.emptyStatement(e1);
        assert !iz.emptyStatement(createTestStatementFromString("int x;"));
    }

    public void testEnumDeclaration() throws Exception {
        PsiElement e1 = createEnumClassFromString("Day");
        assert iz.enumDeclaration(e1);
        assert !iz.enumDeclaration(createTestClassFromString("", "Day", "", "public"));
    }

    public void testInterface() throws Exception {
        PsiElement e1 = createInterfaceClassFromString("A");
        assert iz.interface¢(e1);
        assert !iz.interface¢(createEnumClassFromString("A"));
    }

    public void testStubMethodCall() throws Exception {
        PsiElement e1 = createTestMethodCallExpression("booleanExpression");
        assert iz.stubMethodCall(e1);
        assert !iz.stubMethodCall(createTestMethodCallExpression("foo"));
    }

    public void testConforms() throws Exception {
        PsiElement e1 = createTestExpression("1+5"), e2 = createTestExpression("1 > 5");
        assert iz.conforms(e1, e2);
        GenericPsiExpression ge = new GenericPsiExpression(PsiType.BOOLEAN, e2);
        assert iz.conforms(e1, ge);
        assert !iz.conforms(ge, e1);
        PsiElement e3 = createTestStatementFromString("return x"), e4 = createTestStatementFromString("return y");
        assert iz.conforms(e3, e4);
        GenericPsiStatement gs = new GenericPsiStatement(e4);
        assert iz.conforms(e3, gs);
        assert !iz.conforms(gs, e3);
    }
}