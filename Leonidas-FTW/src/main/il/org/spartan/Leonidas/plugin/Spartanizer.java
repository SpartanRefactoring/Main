package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Oren Afek
 * @since 09-12-2016
 */
public enum Spartanizer {
    ;

    static boolean canTip(PsiElement e) {
        return Toolbox.getInstance().canTip(e);
    }

    static void spartanizeElement(PsiElement e) {
        if (!"SpartanizerUtils.java".equals(e.getContainingFile().getName()))
			Toolbox.getInstance().executeAllTippers(e);
    }

    public static void spartanizeFileOnePass(PsiFile f) {
        if ("SpartanizerUtils.java".equals(f.getName()))
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
}
