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

    public static List<PsiParameter> parameters(PsiMethod m) {
        return m == null ? new ArrayList<>() : parameters(m.getParameterList());
    }

    public static List<PsiParameter> parameters(PsiParameterList method) {
        return method == null ? new ArrayList<>() : Arrays.asList(method.getParameters());
    }

    public static PsiParameter firstParameter(PsiParameterList l) {
        return parameters(l).isEmpty() ? null : l.getParameters()[0];
    }

    public static PsiParameter secondParameter(PsiParameterList l) {
        return parameters(l).size() > 1 ? null : l.getParameters()[1];
    }

    public static PsiParameter firstParameter(PsiMethod m) {
        return m == null ? null : firstParameter(m.getParameterList());
    }

    public static PsiExpression firstParameterExpression(PsiMethodCallExpression x) {
        return x == null || x.getArgumentList().getExpressions().length < 1 ? null : x.getArgumentList().getExpressions()[0];
    }

    public static PsiExpression secondParameterExpression(PsiMethodCallExpression x) {
        return x == null || x.getArgumentList().getExpressions().length < 2 ? null : x.getArgumentList().getExpressions()[1];
    }

    /**
     * Extracts a list of all the arguments present in method call. For example, calling with the
     * following method call expression <code>test(1, "dsa", 3)</code> should return a list
     * containing 1, "dsa" and 3.
     *
     * @param method method call expression from which the arguments should be extracted
     * @return list of method call's arguments
     */
    public static List<PsiExpression> arguments(PsiMethodCallExpression method) {
        return method == null ? null : new ArrayList<>(Arrays.asList(method.getArgumentList().getExpressions()));
    }

    public static PsiStatement firstStatement(PsiCodeBlock b) {
        return b == null || statements(b).isEmpty() ? null : statements(b).get(0);
    }

    public static String name(PsiNamedElement e) {
        return e == null ? null : e.getName();
    }

    public static List<PsiStatement> statements(PsiCodeBlock b) {
        return b == null ? new ArrayList<>() : Arrays.asList(b.getStatements());
    }

    public static PsiCodeBlock blockBody(PsiLambdaExpression x) {
        return !iz.block(x.getBody()) ? null : (PsiCodeBlock) x.getBody();
    }

    public static PsiExpression expression(PsiReturnStatement s) {
        return s == null ? null : s.getReturnValue();
    }

    public static PsiExpression expression(PsiExpressionStatement s) {
        return s == null ? null : s.getExpression();
    }

    public static PsiType returnType(PsiMethod m) {
        return m == null ? null : m.getReturnType();
    }

    public static List<PsiField> fields(PsiClass clazz) {
        return Arrays.asList(clazz.getFields());
    }

    public static PsiExpression conditionExpression(PsiConditionalExpression x) {
        return x == null ? null : x.getCondition();
    }

    public static IElementType operator(PsiBinaryExpression x) {
        return x == null ? null : x.getOperationTokenType();
    }

    public static PsiExpression leftOperand(PsiBinaryExpression x) {

        return x == null ? null : x.getLOperand();
    }

    public static PsiExpression rightOperand(PsiBinaryExpression x) {
        return x == null ? null : x.getROperand();
    }

    public static PsiExpression thenExpression(PsiConditionalExpression x) {
        return x == null ? null : x.getThenExpression();
    }

    public static PsiExpression elseExpression(PsiConditionalExpression x) {
        return x == null ? null : x.getElseExpression();
    }

    public static PsiElement nextSibling(PsiElement e) {
        PsiElement b = e.getNextSibling();
        while (b != null && iz.whiteSpace(b))
            b = b.getNextSibling();
        return b;
    }

    @NotNull
    public static String docCommentString(@NotNull PsiJavaDocumentedElement e) {
        PsiDocComment doc = e.getDocComment();
        return doc == null ? "" : doc.getText().substring(3, doc.getText().length() - 2);
    }

    public static PsiElement getHighestParent(PsiElement e) {
        PsiElement prev = e, next = e.getParent();
        for (; next != null && next.getText().startsWith(prev.getText()); next = next.getParent())
            prev = next;
        return prev;
    }

    /**
     * Extracts the type of the literal the given literal expression holds. Literal types can be,
     * for example, <code>NULL_KEYWORD</code>, <code>TRUE_KEYWORD</code>,
     * <code>INTEGER_LITERAL</code>, etc...
     *
     * @param e literal expression
     * @return type of the literal expression
     * @see com.intellij.psi.JavaTokenType for a full list of possibly types
     */
    public static IElementType literalType(PsiLiteralExpression e) {
        return e == null ? null : ((PsiJavaToken) e.getFirstChild()).getTokenType();
    }

    /**
     * blame: Oren Afek
     * Extracts the name of the method being called.
     * for example:
     * obj.foo(x,y,z) -> foo
     *
     * @param e method call expression
     * @return method name
     */
    public static PsiReferenceExpression methodExpression(PsiMethodCallExpression e) {
        return e == null ? null : e.getMethodExpression();
    }

    public static Optional<PsiClass> clazz(PsiFile f) {
        Wrapper<PsiClass> result = new Wrapper<>(null);
        f.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitClass(PsiClass aClass) {
                result.set(aClass);
            }
        });

        return result.get() == null ? Optional.empty() : Optional.of(result.get());
    }
}
