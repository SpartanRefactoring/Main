package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author RoeiRaz
 * @since 18/06/17
 * <p>
 * SpartanizationBatch is a collection of spartanization work instructions
 * that can be invoked in the future.
 */
public class SpartanizationBatch extends Task.Modal {

    private final static String title = "Spartanization in progress";
    private final Project project;
    private final Set<PsiFile> files;

    SpartanizationBatch(Project project, Set<PsiFile> files) {
        super(project, title, true);
        this.project = project;
        this.files = files;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        Wrapper<Integer> i = new Wrapper<>(0);
        files.forEach(f -> {
            // It's important to run this with Application::invokeLater and with
            // the default modality - this way we have write access to the model
            // through the EDT from which this task was generated.
            ApplicationManager.getApplication().invokeLater(() -> {
                Spartanizer.spartanizeFileRecursively(f);
            });
            i.set(i.get() + 1);
            indicator.setFraction((double) i.get() / files.size());
        });
    }


    public Set<PsiFile> getFiles() {
        return files;
    }
}
