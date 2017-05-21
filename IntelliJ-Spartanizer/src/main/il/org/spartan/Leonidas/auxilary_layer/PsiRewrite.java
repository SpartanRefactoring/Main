package il.org.spartan.Leonidas.auxilary_layer;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.plugin.Toolbox;

/**
 * This class helps performing replacement actions on Psi elements without having to create
 * each time WriteCommandAction.
 *
 * @author Roey Maor
 * @since 03-12-2016
 */
public class PsiRewrite {
    private PsiFile psiFile;
    private Project project;

    public PsiRewrite psiFile(PsiFile ¢) {
        this.psiFile = ¢;
        return this;
    }

    public PsiRewrite project(Project ¢) {
        this.project = ¢;
        return this;
    }

    /**
     * @param element1 the tree to replace.
     * @param element2 the replacing tree.
     * @return the new tree that was inserted to the path of the tree that was replaced.
     */
    public PsiElement replace(PsiElement element1, PsiElement element2) {
        Toolbox.getInstance().replaced = true;
        Wrapper<PsiElement> newElement = new Wrapper<>(null);
        new WriteCommandAction.Simple(project, psiFile) {
            @Override
            protected void run() throws Throwable {
                newElement.set(element1.replace(element2));
            }

        }.execute();
        return newElement.get();
    }
}