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

    public static boolean null$(PsiElement ¢) {
        return iz.literal(¢) && az.literal(¢).textMatches("null");
    }

    public static boolean notNull(PsiElement ¢) {
        return !null$(¢);
    }

    public static boolean statement(PsiElement ¢) {
        return typeCheck(PsiStatement.class, ¢);
    }

    public static boolean block(PsiElement ¢) {
        return typeCheck(PsiCodeBlock.class, ¢);
    }

    public static boolean blockStatement(PsiElement ¢) {
        return typeCheck(PsiBlockStatement.class, ¢);
    }

    public static boolean methodCallExpression(PsiElement ¢) {
        return typeCheck(PsiMethodCallExpression.class, ¢);
    }

    public static boolean declarationStatement(PsiElement ¢) {
        return typeCheck(PsiDeclarationStatement.class, ¢);
    }

    public static boolean enumConstant(PsiElement ¢) {
        return typeCheck(PsiEnumConstantImpl.class, ¢);
    }

    public static boolean fieldDeclaration(PsiElement ¢) {
        return typeCheck(PsiFieldImpl.class, ¢);
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

    public static boolean method(PsiElement ¢) {
        return typeCheck(PsiMethod.class, ¢);
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

    public static boolean returnStatement(PsiElement ¢) {
        return typeCheck(PsiReturnStatementImpl.class, ¢);
    }

    public static boolean type(PsiElement ¢) {
        return typeCheck(PsiTypeElementImpl.class, ¢);
    }

    public static boolean expressionStatement(PsiElement ¢) {
        return typeCheck(PsiExpressionStatementImpl.class, ¢);
    }

    public static boolean identifier(PsiElement ¢) {
        return typeCheck(PsiIdentifier.class, ¢);
    }

    public static boolean conditionalExpression(PsiElement ¢) {
        return typeCheck(PsiConditionalExpressionImpl.class, ¢);
    }

    public static boolean nullExpression(PsiExpression ¢) {
        return "null".equals(¢.getText());
    }

    public static boolean binaryExpression(PsiElement ¢) {
        return typeCheck(PsiBinaryExpressionImpl.class, ¢);
    }

    public static boolean referenceExpression(PsiElement ¢) {
        return typeCheck(PsiReferenceExpressionImpl.class, ¢);
    }

    public static boolean javaCodeReference(PsiElement ¢) {
        return typeCheck(PsiJavaCodeReferenceElement.class, ¢);
    }

    public static boolean equalsOperator(IElementType operator) {
        return operator != null && operator.equals(JavaTokenType.EQEQ);
    }

    public static boolean notEqualsOperator(IElementType operator) {
        return operator != null && operator.equals(JavaTokenType.NE);
    }

    public static boolean literal(PsiElement ¢) {
        return typeCheck(PsiLiteral.class, ¢);
    }

    public static boolean classDeclaration(PsiElement ¢) {
        return typeCheck(PsiClassImpl.class, ¢);
    }

    public static boolean forStatement(PsiElement ¢) {
        return typeCheck(PsiForStatement.class, ¢);
    }

    public static boolean forEachStatement(PsiElement ¢) {
        return typeCheck(PsiForeachStatement.class, ¢);
    }

    public static boolean ifStatement(PsiElement ¢) {
        return typeCheck(PsiIfStatement.class, ¢);
    }

    public static boolean importList(PsiElement ¢) {
        return typeCheck(PsiImportList.class, ¢);
    }

    public static boolean javaToken(PsiElement ¢) {
        return typeCheck(PsiJavaToken.class, ¢);
    }

    public static boolean expression(PsiElement ¢) {
        return typeCheck(PsiExpression.class, ¢);
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
        if (iz.generic(e2))
			return genericConforms(e1, e2, m);
        return new MatchingResult(elseConforms(e1.getInner(), e2.getInner()));
    }

    public static boolean whiteSpace(PsiElement ¢) {
        return typeCheck(PsiWhiteSpace.class, ¢);
    }

    public static boolean ofType(PsiElement e, Class<? extends PsiElement> type) {
        return typeCheck(type, e);
    }

    public static boolean documentedElement(PsiElement ¢) {
        return typeCheck(PsiJavaDocumentedElement.class, ¢);
    }

    public static boolean javadoc(PsiElement ¢) {
        return typeCheck(PsiDocComment.class, ¢);
    }

    public static boolean generic(Encapsulator ¢) {
        return ¢ instanceof GenericEncapsulator && ¢.isGeneric();
    }

    public static boolean whileStatement(PsiElement ¢) {
        return typeCheck(PsiWhileStatement.class, ¢);
    }

    public static boolean switchStatement(PsiElement ¢) {
        return typeCheck(PsiSwitchStatement.class, ¢);
    }

    public static boolean loopStatement(PsiElement ¢) {
        return typeCheck(PsiLoopStatement.class, ¢);
    }

    public static boolean tryStatement(PsiElement ¢) {
        return typeCheck(PsiTryStatement.class, ¢);
    }

    public static boolean enclosingStatement(PsiElement ¢) {
        return ifStatement(¢) || switchStatement(¢) || loopStatement(¢) || tryStatement(¢);
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

    static boolean variable(PsiElement ¢) {
        return typeCheck(PsiVariable.class, ¢);
    }

    static boolean newExpression(PsiElement ¢) {
        return typeCheck(PsiNewExpression.class, ¢);
    }

    public static boolean genericBlock(Encapsulator treeTemplate) {
        return treeTemplate instanceof Block;
    }

    public static boolean genericExpression(Encapsulator ¢) {
        return ¢ instanceof Expression;
    }

    public static boolean genericStatement(Encapsulator ¢) {
        return ¢ instanceof Statement;
    }

    public static boolean genericBooleanLiteral(Encapsulator ¢) {
        return ¢ instanceof BooleanLiteral;
    }

    public static boolean genericMethod(Encapsulator ¢) {
        return ¢ instanceof Method;
    }

    public static boolean anyNumberOf(Encapsulator ¢) {
        return ¢ instanceof AnyNumberOfMethodCallBased;
    }

    public static boolean quantifier(Encapsulator ¢) {
        return ¢ instanceof QuantifierMethodCallBased;
    }

    public static boolean optional(Encapsulator ¢) {
        return ¢ instanceof OptionalMethodCallBased;
    }

    public static boolean modifierListOwner(PsiElement ¢) {
        return typeCheck(PsiModifierListOwner.class, ¢);
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
     * @param x element to check
     * @return <code>true</code> if the element is arithmetic, <code>false</code> otherwise
     */
    public static boolean arithmetic(PsiExpression x) {
        if (iz.literal(x)) {
            IElementType type = az.javaToken(az.literal(x).getFirstChild()).getTokenType();
            return type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == FLOAT_LITERAL;
        }

        if (!iz.binaryExpression(x))
			return false;

        PsiBinaryExpression be = az.binaryExpression(x);
        return arithmetic(be.getLOperand()) && arithmetic(be.getROperand());
    }

    public static boolean comment(PsiElement ¢){
        return typeCheck(PsiComment.class,¢);
    }

    public static boolean psiFile(PsiElement ¢){return typeCheck(PsiFile.class,¢);}

    public static boolean expressionList(PsiElement ¢) {
        return typeCheck(PsiExpressionList.class, ¢);
    }

    public static boolean thisExpression(PsiElement ¢) {
        return typeCheck(PsiThisExpression.class, ¢);
    }

    public static boolean dot(PsiElement ¢) {
        return iz.javaToken(¢) && ".".equals(az.javaToken(¢).getText());
    }

    public static boolean referenceParameterList(PsiElement ¢) {
        return typeCheck(PsiReferenceParameterList.class, ¢);
    }
}
