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
            public void visitIdentifier(PsiIdentifier i) {
                super.visitIdentifier(i);
                if (i.getText().contains("¢"))
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
            public void visitMethod(PsiMethod m) {
                super.visitMethod(m);
                b.inner = !m.getName().equals(name) ? b.inner : Boolean.TRUE;
            }
        });
        assert b.inner != null;
        return b.inner;
    }

    public static boolean equalsOperator(PsiBinaryExpression x) {
        return x != null && iz.equalsOperator(step.operator(x));
    }

    public static boolean notEqualsOperator(PsiBinaryExpression x) {
        return x != null && step.operator(x).equals(JavaTokenType.NE);
    }

    public static boolean compilationErrors(PsiFile f) {
        return haz.syntaxErrors(f) || CompilationCenter.hasCompilationErrors(f);
    }

    public static boolean syntaxErrors(PsiElement e) {
        return (PsiTreeUtil.hasErrorElements(e));
    }

    public static boolean body(PsiMethod m) {
        return m != null && m.getBody() != null;
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
