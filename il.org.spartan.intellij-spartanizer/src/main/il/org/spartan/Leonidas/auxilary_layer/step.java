package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TODO @orenafek comment this class
 *
 * @author Oren Afek
 * @since 01-12-2016
 */
public enum step {
    ;

    public static List<PsiParameter> parameters(PsiMethod ¢) {
        return ¢ == null ? new ArrayList<>() : parameters(¢.getParameterList());
    }

    public static List<PsiParameter> parameters(PsiParameterList method) {
        return method == null ? new ArrayList<>() : Arrays.asList(method.getParameters());
    }

    public static PsiParameter firstParameter(PsiParameterList ¢) {
        return parameters(¢).isEmpty() ? null : ¢.getParameters()[0];
    }

    public static PsiParameter secondParameter(PsiParameterList ¢) {
        return parameters(¢).size() > 1 ? null : ¢.getParameters()[1];
    }

    public static PsiParameter firstParameter(PsiMethod ¢) {
        return ¢ == null ? null : firstParameter(¢.getParameterList());
    }

    public static PsiExpression firstParameterExpression(PsiMethodCallExpression ¢) {
        return ¢ == null || ¢.getArgumentList().getExpressions().length < 1 ? null : ¢.getArgumentList().getExpressions()[0];
    }

    public static PsiExpression secondParameterExpression(PsiMethodCallExpression ¢) {
        return ¢ == null || ¢.getArgumentList().getExpressions().length < 2 ? null : ¢.getArgumentList().getExpressions()[1];
    }

    /**
     * Extracts a list of all the arguments present in method call. For example, calling with the
     * following method call expression <code>test(1, "dsa", 3)</code> should return a list
     * containing 1, "dsa" and 3.
     *
     * @param ¢ method call expression from which the arguments should be extracted
     * @return list of method call's arguments
     */
    public static List<PsiExpression> arguments(PsiMethodCallExpression ¢) {
        return ¢ == null ? null : new ArrayList<>(Arrays.asList(¢.getArgumentList().getExpressions()));
    }

    public static PsiStatement firstStatement(PsiCodeBlock ¢) {
        return ¢ == null || statements(¢).isEmpty() ? null : statements(¢).get(0);
    }

    public static String name(PsiNamedElement ¢) {
        return ¢ == null ? null : ¢.getName();
    }

    public static List<PsiStatement> statements(PsiCodeBlock ¢) {
        return ¢ == null ? new ArrayList<>() : Arrays.asList(¢.getStatements());
    }

    public static PsiCodeBlock blockBody(PsiLambdaExpression ¢) {
        return !iz.block(¢.getBody()) ? null : (PsiCodeBlock) ¢.getBody();
    }

    public static PsiExpression expression(PsiReturnStatement ¢) {
        return ¢ == null ? null : ¢.getReturnValue();
    }

    public static PsiExpression expression(PsiExpressionStatement ¢) {
        return ¢ == null ? null : ¢.getExpression();
    }

    public static PsiType returnType(PsiMethod ¢) {
        return ¢ == null ? null : ¢.getReturnType();
    }

    public static List<PsiField> fields(PsiClass clazz) {
        return Arrays.asList(clazz.getFields());
    }

    public static PsiExpression conditionExpression(PsiConditionalExpression ¢) {
        return ¢ == null ? null : ¢.getCondition();
    }

    public static IElementType operator(PsiBinaryExpression ¢) {
        return ¢ == null ? null : ¢.getOperationTokenType();
    }

    public static PsiExpression leftOperand(PsiBinaryExpression ¢) {

        return ¢ == null ? null : ¢.getLOperand();
    }

    public static PsiExpression rightOperand(PsiBinaryExpression ¢) {
        return ¢ == null ? null : ¢.getROperand();
    }

    public static PsiExpression thenExpression(PsiConditionalExpression ¢) {
        return ¢ == null ? null : ¢.getThenExpression();
    }

    public static PsiExpression elseExpression(PsiConditionalExpression ¢) {
        return ¢ == null ? null : ¢.getElseExpression();
    }

    public static PsiElement nextSibling(PsiElement ¢) {
        PsiElement $ = ¢.getNextSibling();
        while ($ != null && iz.whiteSpace($))
            $ = $.getNextSibling();
        return $;
    }

    @NotNull
    public static String docCommentString(@NotNull PsiJavaDocumentedElement ¢) {
        PsiDocComment $ = ¢.getDocComment();
        return $ == null ? "" : $.getText().substring(3, $.getText().length() - 2);
    }

    public static PsiElement getHighestParent(PsiElement e) {
        PsiElement $ = e, next = e.getParent();
        for (; next != null && next.getText().startsWith($.getText()); next = next.getParent())
            $ = next;
        return $;
    }

    /**
     * Extracts the type of the literal the given literal expression holds. Literal types can be,
     * for example, <code>NULL_KEYWORD</code>, <code>TRUE_KEYWORD</code>,
     * <code>INTEGER_LITERAL</code>, etc...
     *
     * @param ¢ literal expression
     * @return type of the literal expression
     * @see com.intellij.psi.JavaTokenType for a full list of possibly types
     */
    public static IElementType literalType(PsiLiteralExpression ¢) {
        return ¢ == null ? null : ((PsiJavaToken) ¢.getFirstChild()).getTokenType();
    }

    /**
     * blame: Oren Afek
     * Extracts the name of the method being called.
     * for example:
     * obj.foo(x,y,z) -> foo
     *
     * @param ¢ method call expression
     * @return method name
     */
    public static PsiReferenceExpression methodExpression(PsiMethodCallExpression ¢) {
        return ¢ == null ? null : ¢.getMethodExpression();
    }

    public static Optional<PsiClass> clazz(PsiFile f) {
        Wrapper<PsiClass> $ = new Wrapper<>(null);
        f.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitClass(PsiClass aClass) {
                $.set(aClass);
            }
        });

        return $.get() == null ? Optional.empty() : Optional.of($.get());
    }
}
