package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;

/**
 * Helps to get the dynamic type of a Psi element.
 * @author michalcohen
 * @since 31-12-16
 */
public class type {
    public static Class<? extends PsiElement> of(PsiElement x) {
        Wrapper<Class<? extends PsiElement>> myClass = new Wrapper<>(PsiElement.class);
        x.accept(new JavaElementVisitor() {
            @Override
            public void visitClass(PsiClass aClass) {
                super.visitClass(aClass);
                myClass.set(PsiClass.class);
            }

            @Override
            public void visitCallExpression(PsiCallExpression ¢) {
                super.visitCallExpression(¢);
                myClass.set(PsiCallExpression.class);
            }

            @Override
            public void visitMethodReferenceExpression(PsiMethodReferenceExpression ¢) {
                super.visitMethodReferenceExpression(¢);
                myClass.set(PsiMethodReferenceExpression.class);
            }

            @Override
            public void visitMethod(PsiMethod statement) {
                super.visitMethod(statement);
                myClass.set(PsiMethod.class);
            }

            @Override
            public void visitConditionalExpression(PsiConditionalExpression statement) {
                super.visitConditionalExpression(statement);
                myClass.set(PsiConditionalExpression.class);
            }

            @Override
            public void visitIdentifier(PsiIdentifier statement) {
                super.visitIdentifier(statement);
                myClass.set(PsiIdentifier.class);
            }

            @Override
            public void visitForeachStatement(PsiForeachStatement ¢) {
                super.visitForeachStatement(¢);
                myClass.set(PsiForeachStatement.class);
            }

            @Override
            public void visitLambdaExpression(PsiLambdaExpression statement) {
                super.visitLambdaExpression(statement);
                myClass.set(PsiLambdaExpression.class);
            }

            @Override
            public void visitExpression(PsiExpression statement) {
                super.visitExpression(statement);
                myClass.set(PsiExpression.class);
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression statement) {
                super.visitMethodCallExpression(statement);
                myClass.set(PsiMethodCallExpression.class);
            }

            @Override
            public void visitIfStatement(PsiIfStatement ¢) {
                super.visitIfStatement(¢);
                myClass.set(PsiIfStatement.class);
            }

            @Override
            public void visitWhileStatement(PsiWhileStatement ¢) {
                super.visitWhileStatement(¢);
                myClass.set(PsiWhileStatement.class);
            }

            @Override
            public void visitPrefixExpression(PsiPrefixExpression ¢) {
                super.visitPrefixExpression(¢);
                myClass.set(PsiPrefixExpression.class);
            }

            @Override
            public void visitDeclarationStatement(PsiDeclarationStatement ¢) {
                super.visitDeclarationStatement(¢);
                myClass.set(PsiDeclarationStatement.class);
            }

            @Override
            public void visitForStatement(PsiForStatement ¢) {
                super.visitForStatement(¢);
                myClass.set(PsiForStatement.class);
            }
        });
        return myClass.get();
    }

}
