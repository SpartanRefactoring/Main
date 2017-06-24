package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiEnumConstantImpl;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.PsiTypeElementImpl;
import com.intellij.psi.impl.source.tree.java.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.tree.IElementType;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.*;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.lang.Class;
import java.util.List;
import java.util.Map;

import static com.intellij.psi.JavaTokenType.*;
import static com.intellij.psi.PsiModifier.PUBLIC;
import static com.intellij.psi.PsiModifier.STATIC;

/**
 * Utils class that helps to check if a Psi element iz of a specific type
 *
 * @author Oren Afek
 * @since 01-12-2016
 */
public enum iz {
    ;
    private static final String ABSTRACT = "abstract";

    private static boolean typeCheck(Class<? extends PsiElement> type, PsiElement e) {
        return e != null && type.isAssignableFrom(e.getClass());
    }

    public static boolean null$(PsiElement e) {
        return iz.literal(e) && az.literal(e).textMatches("null");
    }

    public static boolean notNull(PsiElement e) {
        return !null$(e);
    }

    public static boolean statement(PsiElement e) {
        return typeCheck(PsiStatement.class, e);
    }

    public static boolean block(PsiElement e) {
        return typeCheck(PsiCodeBlock.class, e);
    }

    public static boolean blockStatement(PsiElement e) {
        return typeCheck(PsiBlockStatement.class, e);
    }

    public static boolean methodCallExpression(PsiElement e) {
        return typeCheck(PsiMethodCallExpression.class, e);
    }

    public static boolean declarationStatement(PsiElement e) {
        return typeCheck(PsiDeclarationStatement.class, e);
    }

    public static boolean enumConstant(PsiElement e) {
        return typeCheck(PsiEnumConstantImpl.class, e);
    }

    public static boolean fieldDeclaration(PsiElement e) {
        return typeCheck(PsiFieldImpl.class, e);
    }

    public static boolean abstract$(PsiMethod element) {
        return element.getModifierList().hasExplicitModifier(ABSTRACT);
    }

    public static boolean static$(PsiMethod element) {
        return element.getModifierList().hasExplicitModifier(STATIC);
    }

    public static boolean singleParameterMethod(PsiMethod element) {
        return element.getParameterList().getParameters().length == 1;
    }

    public static boolean method(PsiElement e) {
        return typeCheck(PsiMethod.class, e);
    }

    public static boolean void$(PsiMethod element) {
        return PsiType.VOID.equals(step.returnType(element));
    }

    public static boolean public$(PsiMethod element) {
        return element != null && element.getModifierList().hasExplicitModifier(PUBLIC);
    }

    public static boolean main(PsiMethod element) {
        return element != null &&
                public$(element) &&
                static$(element) &&
                "main".equals(element.getName()) &&
                iz.singleParameterMethod(element) &&
                "args".equals(step.name(step.firstParameter(element)));
    }

    public static boolean returnStatement(PsiElement e) {
        return typeCheck(PsiReturnStatementImpl.class, e);
    }

    public static boolean type(PsiElement e) {
        return typeCheck(PsiTypeElementImpl.class, e);
    }

    public static boolean expressionStatement(PsiElement e) {
        return typeCheck(PsiExpressionStatementImpl.class, e);
    }

    public static boolean identifier(PsiElement e) {
        return typeCheck(PsiIdentifier.class, e);
    }

    public static boolean conditionalExpression(PsiElement e) {
        return typeCheck(PsiConditionalExpressionImpl.class, e);
    }

    public static boolean nullExpression(PsiExpression x) {
        return "null".equals(x.getText());
    }

    public static boolean binaryExpression(PsiElement e) {
        return typeCheck(PsiBinaryExpressionImpl.class, e);
    }

    public static boolean referenceExpression(PsiElement e) {
        return typeCheck(PsiReferenceExpressionImpl.class, e);
    }

    public static boolean javaCodeReference(PsiElement e) {
        return typeCheck(PsiJavaCodeReferenceElement.class, e);
    }

    public static boolean equalsOperator(IElementType operator) {
        return operator != null && operator.equals(JavaTokenType.EQEQ);
    }

    public static boolean notEqualsOperator(IElementType operator) {
        return operator != null && operator.equals(JavaTokenType.NE);
    }

    public static boolean literal(PsiElement e) {
        return typeCheck(PsiLiteral.class, e);
    }

    public static boolean classDeclaration(PsiElement e) {
        return typeCheck(PsiClassImpl.class, e);
    }

    public static boolean forStatement(PsiElement e) {
        return typeCheck(PsiForStatement.class, e);
    }

    public static boolean forEachStatement(PsiElement e) {
        return typeCheck(PsiForeachStatement.class, e);
    }

    public static boolean ifStatement(PsiElement e) {
        return typeCheck(PsiIfStatement.class, e);
    }

    public static boolean importList(PsiElement e) {
        return typeCheck(PsiImportList.class, e);
    }

    public static boolean javaToken(PsiElement e) {
        return typeCheck(PsiJavaToken.class, e);
    }

    public static boolean expression(PsiElement e) {
        return typeCheck(PsiExpression.class, e);
    }

    /**
     * @return true if e1 is of e2's type or inherits from it
     */
    public static boolean theSameType(PsiElement e1, PsiElement e2) {
        return typeCheck(e2.getClass(), e1);
    }


    private static boolean literalConforms(PsiElement e1, PsiElement e2) {
        return (iz.literal(e1) && iz.literal(e2) && az.literal(e1).getText().equals(az.literal(e2).getText()));
    }

    private static boolean tokenConforms(PsiElement e1, PsiElement e2) {
        return (iz.javaToken(e1) && iz.javaToken(e2) && az.javaToken(e1).getText().equals(az.javaToken(e2).getText()));
    }

    private static MatchingResult genericConforms(Encapsulator e1, Encapsulator e2, Map<Integer, List<PsiElement>> m) {
        if (!iz.generic(e2)) return new MatchingResult(false);
        return az.generic(e2).generalizes(e1, m);
    }

    private static boolean elseConforms(PsiElement e1, PsiElement e2) {
        return !iz.literal(e1) && !iz.literal(e2) && !iz.javaToken(e1) && !iz.javaToken(e2) && iz.theSameType(e1, e2);
    }

    /**
     * e2 is the generic tree
     */
    public static MatchingResult conforms(Encapsulator e1, Encapsulator e2, Map<Integer, List<PsiElement>> m) {
        if (literalConforms(e1.getInner(), e2.getInner()) || tokenConforms(e1.getInner(), e2.getInner()))
            return new MatchingResult(true);
        if (iz.generic(e2)){
            return genericConforms(e1, e2, m);
        }
        return new MatchingResult(elseConforms(e1.getInner(), e2.getInner()));
    }

    public static boolean whiteSpace(PsiElement e) {
        return typeCheck(PsiWhiteSpace.class, e);
    }

    public static boolean ofType(PsiElement e, Class<? extends PsiElement> type) {
        return typeCheck(type, e);
    }

    public static boolean documentedElement(PsiElement e) {
        return typeCheck(PsiJavaDocumentedElement.class, e);
    }

    public static boolean javadoc(PsiElement e) {
        return typeCheck(PsiDocComment.class, e);
    }

    public static boolean generic(Encapsulator e) {
        return e instanceof GenericEncapsulator && e.isGeneric();
    }

    public static boolean whileStatement(PsiElement e) {
        return typeCheck(PsiWhileStatement.class, e);
    }

    public static boolean switchStatement(PsiElement e) {
        return typeCheck(PsiSwitchStatement.class, e);
    }

    public static boolean loopStatement(PsiElement e) {
        return typeCheck(PsiLoopStatement.class, e);
    }

    public static boolean tryStatement(PsiElement e) {
        return typeCheck(PsiTryStatement.class, e);
    }

    public static boolean enclosingStatement(PsiElement e) {
        return ifStatement(e) || switchStatement(e) || loopStatement(e) || tryStatement(e);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean synchronized¢(final PsiMember ¢) {
        return (¢.getModifierList().hasModifierProperty(PsiModifier.SYNCHRONIZED));
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean native¢(final PsiMember ¢) {
        return (¢.getModifierList().hasModifierProperty(PsiModifier.NATIVE));
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean default¢(final PsiMember ¢) {
        return (¢.getModifierList().hasModifierProperty(PsiModifier.DEFAULT));
    }


    public static boolean annotation(final PsiElement ¢) {
        return typeCheck(PsiAnnotation.class, ¢);
    }

    public static boolean anonymousClassDeclaration(final PsiElement ¢) {
        return typeCheck(PsiAnonymousClass.class, ¢);
    }

    public static boolean arrayInitializer(final PsiElement ¢) {
        return typeCheck(PsiArrayInitializerExpression.class, ¢);
    }

    /**
     * @param ¢ pattern the statement or block to check if it is an assignment
     * @return <code><b>true</b></code> if the parameter an assignment or false if
     * the parameter not or if the block Contains more than one
     * statement
     */
    public static boolean assignment(final PsiElement ¢) {
        return typeCheck(PsiAssignmentExpression.class, ¢);
    }

    /**
     * Determine whether a node is a boolean literal
     *
     * @param ¢ pattern JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is a boolean
     * literal
     */
    public static boolean booleanLiteral(final PsiElement ¢) {
        return typeCheck(PsiLiteralExpression.class, ¢) && ((PsiLiteralExpression) ¢).getValue() instanceof Boolean;
    }

    /**
     * Determine whether a node is a boolean literal
     *
     * @param ¢ pattern JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is a boolean
     * literal
     */
    public static boolean stringLiteral(final PsiElement ¢) {
        return typeCheck(PsiLiteralExpression.class, ¢) && ((PsiLiteralExpression) ¢).getValue() instanceof String;
    }

    /**
     * @param ¢ JD
     * @return true iff ¢ is "boolean"
     */
    public static boolean booleanType(final PsiType ¢) {
        return ¢ != null && ¢ instanceof PsiPrimitiveType && ¢.getPresentableText().equals(PsiType.BOOLEAN.getPresentableText());
    }

    public static boolean breakStatement(final PsiElement ¢) {
        return typeCheck(PsiBreakStatement.class, ¢);
    }

    /**
     * @param ¢ JD
     * @return true iff ¢ is casting
     */
    public static boolean castExpression(final PsiElement ¢) {
        return typeCheck(PsiTypeCastExpression.class, ¢);
    }

    /**
     * @param ¢ JD
     * @return true iff ¢ is new instance creation
     */
    public static boolean classInstanceCreation(final PsiElement ¢) {
        return typeCheck(PsiNewExpression.class, ¢);
    }

    /**
     * Check whether an expression is a "conditional and" (&&)
     *
     * @param ¢ JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
     * whose operator is AND
     */
    public static boolean conditionalAnd(final PsiBinaryExpression ¢) {
        return ¢.getOperationSign().getTokenType().equals(JavaTokenType.ANDAND);
    }

    /**
     * Check whether an expression is a "conditional or" (||)
     *
     * @param ¢ JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
     * whose operator is OR
     */
    public static boolean conditionalOr(final PsiBinaryExpression ¢) {
        return ¢.getOperationSign().getTokenType().equals(JavaTokenType.OROR);
    }

    /**
     * Determine whether a node is an EmptyStatement
     *
     * @param ¢ pattern JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is an
     * empty statement
     */
    static boolean emptyStatement(final PsiElement ¢) {
        return typeCheck(PsiEmptyStatement.class, ¢);
    }

    static boolean enumDeclaration(final PsiElement ¢) {
        return typeCheck(PsiClass.class, ¢) && (az.classDeclaration(¢).isEnum());
    }

    public static boolean codeBlock(final PsiElement ¢) {
        return typeCheck(PsiCodeBlock.class, ¢);
    }

    static boolean fieldDeclaration(final PsiMember ¢) {
        return typeCheck(PsiField.class, ¢);
    }

    /**
     * Determine whether a declaration is final or not
     *
     * @param ¢ JD
     * @return <code><b>true</b></code> <em>iff</em>declaration is final
     */
    @SuppressWarnings("ConstantConditions")
    static boolean final¢(final PsiMember ¢) {
        return (¢.getModifierList().hasModifierProperty(PsiModifier.FINAL));
    }

    /**
     * Determine whether a variable declaration is final or not
     *
     * @param ¢ JD
     * @return <code><b>true</b></code> <i>iff</i> the variable is declared as
     * final
     */
    @SuppressWarnings("ConstantConditions")
    static boolean final¢(final PsiVariable ¢) {
        return ¢ != null && ¢.getModifierList().hasModifierProperty(PsiModifier.FINAL);
    }

    /**
     * @param ¢ JD
     * @return <code><b>true</b></code> <em>iff</em>the given node is an interface
     * or false otherwise
     */
    static boolean interface¢(final PsiElement ¢) {
        return typeCheck(PsiClass.class, ¢) && (az.classDeclaration(¢).isInterface());
    }

    static boolean variable(PsiElement e) {
        return typeCheck(PsiVariable.class, e);
    }

    static boolean newExpression(PsiElement e) {
        return typeCheck(PsiNewExpression.class, e);
    }

    public static boolean genericBlock(Encapsulator treeTemplate) {
        return treeTemplate instanceof Block;
    }

    public static boolean genericExpression(Encapsulator e) {
        return e instanceof Expression;
    }

    public static boolean genericStatement(Encapsulator e) {
        return e instanceof Statement;
    }

    public static boolean genericBooleanLiteral(Encapsulator e) {
        return e instanceof BooleanLiteral;
    }

    public static boolean genericMethod(Encapsulator e) {
        return e instanceof Method;
    }

    public static boolean anyNumberOf(Encapsulator e) {
        return e instanceof AnyNumberOfMethodCallBased;
    }

    public static boolean quantifier(Encapsulator e) {
        return e instanceof QuantifierMethodCallBased;
    }

    public static boolean optional(Encapsulator e) {
        return e instanceof OptionalMethodCallBased;
    }

    public static boolean modifierListOwner(PsiElement e) {
        return typeCheck(PsiModifierListOwner.class, e);
    }

    /**
     * Checks whether the given element is arithmetic or not. An arithmetic element is one that
     * contains only literal numerical elements, connected by mathematical operators. The following
     * are examples of arithmetic expressions:
     *
     *  3
     *  3 + 4
     *  5 * 6
     *
     * @param e element to check
     * @return <code>true</code> if the element is arithmetic, <code>false</code> otherwise
     */
    public static boolean arithmetic(PsiExpression e) {
        if (iz.literal(e)) {
            IElementType type = az.javaToken(az.literal(e).getFirstChild()).getTokenType();
            return type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == FLOAT_LITERAL;
        }

        if (!iz.binaryExpression(e)) {
            return false;
        }

        PsiBinaryExpression be = az.binaryExpression(e);
        return arithmetic(be.getLOperand()) && arithmetic(be.getROperand());
    }

    public static boolean comment(PsiElement e){
        return typeCheck(PsiComment.class,e);
    }

    public static boolean psiFile(PsiElement e){return typeCheck(PsiFile.class,e);}

    public static boolean expressionList(PsiElement e) {
        return typeCheck(PsiExpressionList.class, e);
    }

    public static boolean thisExpression(PsiElement e) {
        return typeCheck(PsiThisExpression.class, e);
    }

    public static boolean dot(PsiElement e) {
        return iz.javaToken(e) && az.javaToken(e).getText().equals(".");
    }

    public static boolean referenceParameterList(PsiElement e) {
        return typeCheck(PsiReferenceParameterList.class, e);
    }
}
