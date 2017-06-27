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

    public static PsiStatement statement(PsiElement ¢) {
        return ¢ == null || !iz.statement(¢) ? null : (PsiStatement) ¢;
    }

    public static PsiCodeBlock block(PsiElement ¢) {
        return ¢ == null || !iz.block(¢) ? null : (PsiCodeBlock) ¢;
    }

    public static PsiBlockStatement blockStatement(PsiElement ¢) {
        return ¢ == null || !iz.blockStatement(¢) ? null : (PsiBlockStatement) ¢;
    }

    public static PsiDeclarationStatement declarationStatement(PsiElement ¢) {
        return ¢ == null || !iz.declarationStatement(¢) ? null : (PsiDeclarationStatement) ¢;
    }

    public static PsiEnumConstant enumConstant(PsiElement ¢) {
        return ¢ == null || !iz.enumConstant(¢) ? null : (PsiEnumConstant) ¢;
    }

    public static PsiField fieldDeclaration(PsiElement ¢) {
        return ¢ == null || !iz.fieldDeclaration(¢) ? null : (PsiField) ¢;
    }

    public static PsiExpressionStatement expressionStatement(PsiElement ¢) {
        return ¢ == null || !iz.expressionStatement(¢) ? null : (PsiExpressionStatement) ¢;
    }

    public static PsiIdentifier identifier(PsiElement ¢) {
        return ¢ == null || !iz.identifier(¢) ? null : (PsiIdentifier) ¢;
    }

    public static PsiConditionalExpression conditionalExpression(PsiElement ¢) {
        return ¢ == null || !iz.conditionalExpression(¢) ? null : (PsiConditionalExpression) ¢;
    }

    public static PsiBinaryExpression binaryExpression(PsiElement ¢) {
        return ¢ == null || !iz.binaryExpression(¢) ? null : (PsiBinaryExpression) ¢;
    }

    public static PsiReferenceExpression referenceExpression(PsiElement ¢) {
        return ¢ == null || !iz.referenceExpression(¢) ? null : (PsiReferenceExpression) ¢;
    }

    public static PsiJavaCodeReferenceElement javaCodeReference(PsiElement ¢) {
        return ¢ == null || !iz.javaCodeReference(¢) ? null : (PsiJavaCodeReferenceElement) ¢;
    }

    public static PsiLiteral literal(PsiElement ¢) {
        return ¢ == null || !iz.literal(¢) ? null : (PsiLiteral) ¢;
    }

    public static PsiClass classDeclaration(PsiElement ¢) {
        return ¢ == null || !iz.classDeclaration(¢) ? null : (PsiClass) ¢;
    }

    public static PsiForeachStatement forEachStatement(PsiElement ¢) {
        return ¢ == null || !iz.forEachStatement(¢) ? null : (PsiForeachStatement) ¢;
    }

    public static PsiIfStatement ifStatement(PsiElement ¢) {
        return ¢ == null || !iz.ifStatement(¢) ? null : (PsiIfStatement) ¢;
    }

    public static PsiReturnStatement returnStatement(PsiElement ¢) {
        return ¢ == null || !iz.returnStatement(¢) ? null : (PsiReturnStatement) ¢;
    }

    public static PsiImportList importList(PsiElement ¢) {
        return ¢ == null || !iz.importList(¢) ? null : (PsiImportList) ¢;
    }

    public static PsiJavaToken javaToken(PsiElement ¢) {
        return ¢ == null || !iz.javaToken(¢) ? null : (PsiJavaToken) ¢;
    }

    public static PsiMethodCallExpression methodCallExpression(PsiElement ¢) {
        return ¢ == null || !iz.methodCallExpression(¢) ? null : (PsiMethodCallExpression) ¢;
    }

    public static <T extends PsiElement> Integer integer(T value){
        return Integer.valueOf(value.getText());
    }

    public static PsiMethod method(PsiElement ¢) {
        return ¢ == null || !iz.method(¢) ? null : (PsiMethod) ¢;
    }

    public static GenericEncapsulator generic(Encapsulator ¢) {
        return ¢ == null ? null : (GenericEncapsulator) ¢;
    }

    public static PsiNewExpression newExpression(PsiElement ¢) {
        return ¢ == null || !iz.newExpression(¢) ? null : (PsiNewExpression) ¢;
    }

    public static PsiExpression expression(PsiElement ¢) {
        return ¢ == null || !iz.expression(¢) ? null : (PsiExpression) ¢;
    }

    public static OptionalMethodCallBased optional(Encapsulator ¢) {
        return ¢ == null ? null : (OptionalMethodCallBased) ¢;
    }

    public static AnyNumberOfMethodCallBased anyNumberOf(Encapsulator ¢) {
        return ¢ == null ? null : (AnyNumberOfMethodCallBased) ¢;
    }

    public static QuantifierMethodCallBased quantifier(Encapsulator ¢) {
        return ¢ == null ? null : (QuantifierMethodCallBased) ¢;
    }

    public static PsiDocComment javadoc(PsiElement ¢){
        return ¢ == null || !iz.javadoc(¢) ? null : (PsiDocComment) ¢;
    }

    public static PsiModifierListOwner modifierListOwner(PsiElement ¢) {
        return ¢ != null && !iz.modifierListOwner(¢) ? null : (PsiModifierListOwner) ¢;
    }

    public static PsiTypeElement type(PsiElement ¢) {
        return ¢ != null && !iz.type(¢) ? null : (PsiTypeElement) ¢;
    }

    public static PsiComment comment(PsiElement ¢) {
        return ¢ != null && !iz.comment(¢) ? null : (PsiComment) ¢;
    }

    public static PsiFile psiFile(PsiElement ¢) {
        return ¢ == null ? null : (PsiFile) ¢;
    }

    public static <T extends PsiElement> String string(T value) {
        return String.valueOf(value.getText());
    }
}