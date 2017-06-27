package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author RoeiRaz
 * @since 18/06
 */
public class SpartanizeProjectAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {

        // Collect all project PsiFiles
        List<PsiFile> psiFiles = new ArrayList<>();

        ProjectFileIndex.SERVICE.getInstance(e.getProject()).iterateContent(new ContentIterator() {
            @Override
            public boolean processFile(VirtualFile fileOrDir) {
                Optional.ofNullable(PsiManager.getInstance(e.getProject()).findFile(fileOrDir))
                        .filter(λ -> λ instanceof PsiJavaFile).ifPresent(psiFiles::add);
                return true;
            }
        });

        ProgressManager.getInstance().run(new SpartanizationBatch(e.getProject(), new HashSet<>(psiFiles)));

    }
}
