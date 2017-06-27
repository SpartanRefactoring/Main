package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.*;

/**
 * Utils class that helps converting Psi element to a specific Psi type
 * @author Oren Afek
 * @since 01-12-16
 */
public enum az {
    ;

    public static PsiStatement statement(PsiElement e) {
        return e == null || !iz.statement(e) ? null : (PsiStatement) e;
    }

    public static PsiCodeBlock block(PsiElement e) {
        return e == null || !iz.block(e) ? null : (PsiCodeBlock) e;
    }

    public static PsiBlockStatement blockStatement(PsiElement e) {
        return e == null || !iz.blockStatement(e) ? null : (PsiBlockStatement) e;
    }

    public static PsiDeclarationStatement declarationStatement(PsiElement e) {
        return e == null || !iz.declarationStatement(e) ? null : (PsiDeclarationStatement) e;
    }

    public static PsiEnumConstant enumConstant(PsiElement e) {
        return e == null || !iz.enumConstant(e) ? null : (PsiEnumConstant) e;
    }

    public static PsiField fieldDeclaration(PsiElement e) {
        return e == null || !iz.fieldDeclaration(e) ? null : (PsiField) e;
    }

    public static PsiExpressionStatement expressionStatement(PsiElement e) {
        return e == null || !iz.expressionStatement(e) ? null : (PsiExpressionStatement) e;
    }

    public static PsiIdentifier identifier(PsiElement e) {
        return e == null || !iz.identifier(e) ? null : (PsiIdentifier) e;
    }

    public static PsiConditionalExpression conditionalExpression(PsiElement e) {
        return e == null || !iz.conditionalExpression(e) ? null : (PsiConditionalExpression) e;
    }

    public static PsiBinaryExpression binaryExpression(PsiElement e) {
        return e == null || !iz.binaryExpression(e) ? null : (PsiBinaryExpression) e;
    }

    public static PsiReferenceExpression referenceExpression(PsiElement e) {
        return e == null || !iz.referenceExpression(e) ? null : (PsiReferenceExpression) e;
    }

    public static PsiJavaCodeReferenceElement javaCodeReference(PsiElement e) {
        return e == null || !iz.javaCodeReference(e) ? null : (PsiJavaCodeReferenceElement) e;
    }

    public static PsiLiteral literal(PsiElement e) {
        return e == null || !iz.literal(e) ? null : (PsiLiteral) e;
    }

    public static PsiClass classDeclaration(PsiElement e) {
        return e == null || !iz.classDeclaration(e) ? null : (PsiClass) e;
    }

    public static PsiForeachStatement forEachStatement(PsiElement e) {
        return e == null || !iz.forEachStatement(e) ? null : (PsiForeachStatement) e;
    }

    public static PsiIfStatement ifStatement(PsiElement e) {
        return e == null || !iz.ifStatement(e) ? null : (PsiIfStatement) e;
    }

    public static PsiReturnStatement returnStatement(PsiElement e) {
        return e == null || !iz.returnStatement(e) ? null : (PsiReturnStatement) e;
    }

    public static PsiImportList importList(PsiElement e) {
        return e == null || !iz.importList(e) ? null : (PsiImportList) e;
    }

    public static PsiJavaToken javaToken(PsiElement e) {
        return e == null || !iz.javaToken(e) ? null : (PsiJavaToken) e;
    }

    public static PsiMethodCallExpression methodCallExpression(PsiElement e) {
        return e == null || !iz.methodCallExpression(e) ? null : (PsiMethodCallExpression) e;
    }

    public static <T extends PsiElement> Integer integer(T value){
        return Integer.valueOf(value.getText());
    }

    public static PsiMethod method(PsiElement e) {
        return e == null || !iz.method(e) ? null : (PsiMethod) e;
    }

    public static GenericEncapsulator generic(Encapsulator e) {
        return e == null ? null : (GenericEncapsulator) e;
    }

    public static PsiNewExpression newExpression(PsiElement e) {
        return e == null || !iz.newExpression(e) ? null : (PsiNewExpression) e;
    }

    public static PsiExpression expression(PsiElement e) {
        return e == null || !iz.expression(e) ? null : (PsiExpression) e;
    }

    public static OptionalMethodCallBased optional(Encapsulator e) {
        return e == null ? null : (OptionalMethodCallBased) e;
    }

    public static AnyNumberOfMethodCallBased anyNumberOf(Encapsulator e) {
        return e == null ? null : (AnyNumberOfMethodCallBased) e;
    }

    public static QuantifierMethodCallBased quantifier(Encapsulator e) {
        return e == null ? null : (QuantifierMethodCallBased) e;
    }

    public static PsiDocComment javadoc(PsiElement e){
        return e == null || !iz.javadoc(e) ? null : (PsiDocComment) e;
    }

    public static PsiModifierListOwner modifierListOwner(PsiElement e) {
        return e != null && !iz.modifierListOwner(e) ? null : (PsiModifierListOwner) e;
    }

    public static PsiTypeElement type(PsiElement e) {
        return e != null && !iz.type(e) ? null : (PsiTypeElement) e;
    }

    public static PsiComment comment(PsiElement e) {
        return e != null && !iz.comment(e) ? null : (PsiComment) e;
    }

    public static PsiFile psiFile(PsiElement e) {
        return e == null ? null : (PsiFile) e;
    }

    public static <T extends PsiElement> String string(T value) {
        return String.valueOf(value.getText());
    }
}