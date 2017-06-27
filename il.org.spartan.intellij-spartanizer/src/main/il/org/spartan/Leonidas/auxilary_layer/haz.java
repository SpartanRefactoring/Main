package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Utils class that helps checking if a Psi tree has a specific component.
 * @author Michal Cohen
 * @since 01-12-2016
 */
public enum haz {
    ;

    public static boolean centVariableDefinition(final PsiElement e) {
        final Wrapper<Boolean> b = new Wrapper<>(Boolean.FALSE);
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitIdentifier(PsiIdentifier ¢) {
                super.visitIdentifier(¢);
                if (¢.getText().contains("¢"))
					b.set(true);
            }
        });
        assert b.inner != null;
        return b.inner;
    }

    public static boolean functionNamed(final PsiElement e, String name) {
        final Wrapper<Boolean> b = new Wrapper<>(Boolean.FALSE);
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethod(PsiMethod ¢) {
                super.visitMethod(¢);
                b.inner = !¢.getName().equals(name) ? b.inner : Boolean.TRUE;
            }
        });
        assert b.inner != null;
        return b.inner;
    }

    public static boolean equalsOperator(PsiBinaryExpression ¢) {
        return ¢ != null && iz.equalsOperator(step.operator(¢));
    }

    public static boolean notEqualsOperator(PsiBinaryExpression ¢) {
        return ¢ != null && step.operator(¢).equals(JavaTokenType.NE);
    }

    public static boolean compilationErrors(PsiFile ¢){
        return haz.syntaxErrors(¢) || CompilationCenter.hasCompilationErrors(¢);
    }

    public static boolean syntaxErrors(PsiElement ¢) {
        return (PsiTreeUtil.hasErrorElements(¢));
    }

    public static boolean body(PsiMethod ¢){
        return ¢ != null && ¢.getBody() != null;
    }

    public static boolean publicModifier(PsiModifierListOwner mlo){
        return mlo != null && mlo.getModifierList().hasExplicitModifier("public");
    }

    public static boolean privateModifier(PsiModifierListOwner mlo){
        return mlo != null && mlo.getModifierList().hasExplicitModifier("private");
    }

    public static boolean protectedModifier(PsiModifierListOwner mlo){
        return mlo != null && mlo.getModifierList().hasExplicitModifier("protected");
    }

    public static boolean staticModifier(PsiModifierListOwner mlo){
        return mlo != null && mlo.getModifierList().hasExplicitModifier("static");
    }

    public static boolean finalModifier(PsiModifierListOwner mlo){
        return mlo != null && mlo.getModifierList().hasExplicitModifier("final");
    }

    public static boolean abstractModifier(PsiModifierListOwner mlo){
        return mlo != null && mlo.getModifierList().hasExplicitModifier("abstract");
    }

}
