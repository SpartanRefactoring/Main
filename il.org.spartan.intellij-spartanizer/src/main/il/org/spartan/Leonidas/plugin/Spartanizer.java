package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.SpartaDefeat;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

/**
 * @author Oren Afek, Roey Maor
 * @since 09-12-2016
 */
public enum Spartanizer {
    ;

    static boolean canTip(PsiElement e) {
        return Toolbox.getInstance().canTip(e);
    }

    static boolean shouldSpartanize(PsiElement e) {
        PsiFile f = iz.psiFile(e) ? az.psiFile(e) : e.getContainingFile();
        return !"SpartanizerUtils.java".equals(f.getName()) && !isAnnotatedWithSpartaDefeat(f);
    }

    static void spartanizeElement(PsiElement e) {
        if (shouldSpartanize(e))
            Toolbox.getInstance().executeAllTippers(e);
    }

    public static void spartanizeFileOnePass(PsiFile f) {
        if (shouldSpartanize(f))
            return;
        Toolbox toolbox = Toolbox.getInstance();
        toolbox.replaced = true;
        while (toolbox.replaced) {
            toolbox.replaced = false;
            f.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitElement(PsiElement e) {
                    super.visitElement(e);
                    toolbox.executeAllTippers(e);
                }
            });
        }
    }

    public static void spartanizeElementWithTipper(PsiElement e, String tipperName) {
        Toolbox toolbox = Toolbox.getInstance();
        for (Tipper t : toolbox.getAllTippers()) {
            System.out.println(t.name());
        }
    }

    private static boolean isAnnotatedWithSpartaDefeat(PsiFile file) {
        Wrapper<Boolean> result = new Wrapper<>(false);
        file.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitAnnotation(PsiAnnotation annotation) {
                result.set(SpartaDefeat.class.getSimpleName().equals(annotation.getQualifiedName()));
            }
        });

        return result.get();
    }
}
