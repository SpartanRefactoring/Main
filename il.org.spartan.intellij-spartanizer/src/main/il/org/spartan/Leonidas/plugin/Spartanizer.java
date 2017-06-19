package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.SpartaDefeat;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import static il.org.spartan.Leonidas.auxilary_layer.Utils.isAnnotationPresent;

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
        return !"SpartanizerUtils.java".equals(f.getName()) && !isAnnotationPresent(f, SpartaDefeat.class);
    }

    static void spartanizeRecursively(PsiElement e) {
        if (!shouldSpartanize(e))
            return;
        Toolbox toolbox = Toolbox.getInstance();
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement e) {
                super.visitElement(e);
                toolbox.executeAllTippers(e);
            }
        });
    }

    static void spartanizeElement(PsiElement e) {
        if (shouldSpartanize(e))
            Toolbox.getInstance().executeAllTippers(e);
    }

    static void spartanizeElement(PsiElement e, Tipper tipper) {
        if (shouldSpartanize(e))
            Toolbox.getInstance().executeTipper(e, tipper);
    }

    public static void spartanizeFileOnePass(PsiFile f) {
        if (!shouldSpartanize(f))
            return;
        Toolbox toolbox = Toolbox.getInstance();
        f.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement e) {
                super.visitElement(e);
                toolbox.executeAllTippers(e);
            }
        });
    }

    // TODO this is a bad name. its not recursive.
    public static void spartanizeFileRecursively(PsiFile f) {
        Toolbox toolbox = Toolbox.getInstance();
        toolbox.replaced = true;
        while (toolbox.replaced) {
            toolbox.replaced = false;
            spartanizeFileOnePass(f);
        }
    }


    public static void spartanizeElementWithTipper(PsiElement e, String tipperName) {
        Toolbox toolbox = Toolbox.getInstance();
        for (Tipper t : toolbox.getAllTippers()) {
            System.out.println(t.name());
        }
    }
}
