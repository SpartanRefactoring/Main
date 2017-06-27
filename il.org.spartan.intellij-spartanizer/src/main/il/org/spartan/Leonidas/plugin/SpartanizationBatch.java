package il.org.spartan.Leonidas.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import icons.Icons;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author RoeiRaz
 * @since 18/06/17
 * <p>
 * SpartanizationBatch is a collection of spartanization work instructions
 * that can be invoked in the future.
 */
public class SpartanizationBatch extends Task.Modal {

    private static final String title = "Spartanization in progress";
    private final Project project;
    private final Set<PsiFile> files;
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    SpartanizationBatch(Project project, Set<PsiFile> files) {
        super(project, title, true);
        this.project = project;
        this.files = files;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        Wrapper<Integer> i = new Wrapper<>(0);
        Wrapper<Throwable> fault = new Wrapper<>(null);

        for (PsiFile f : files) {
            // It's important to run this with Application::invokeLater and with
            // the default modality - this way we have write access to the model
            // through the EDT from which this task was generated.
            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    Spartanizer.spartanizeFileRecursively(f);
                } catch (Throwable ¢) {
                    // we catch all throwables to prevent deadlock further ahead.
                    fault.set(¢);
                } finally {
                    atomicBoolean.set(true);
                }
            });

            // spinlock. im sorry
            while (!atomicBoolean.get()) Thread.yield();
            atomicBoolean.set(false);

            // terminate in case of error (that is why we use foreach)
            if (fault.get() != null) {
                Notifications.Bus.notify(new Notification("il.org.spartan",
                        Icons.LeonidasSmall,
                        "Spartanizer",
                        "Spartanization failed. faulty file: " + f.getName() + ". Exception: " + fault.get(),
                        null,
                        NotificationType.ERROR,
                        null));
                return;
            }

            // update progress bar
            i.set(i.get() + 1);
            indicator.setFraction((double) i.get() / files.size());
        }

        // Send success notification
        Notifications.Bus.notify(new Notification("il.org.spartan",
                Icons.LeonidasSmall,
                "Spartanizer",
                "Finished spartanization of " + getFiles().size() + " files.",
                null,
                NotificationType.INFORMATION,
                null));
    }


    public Set<PsiFile> getFiles() {
        return files;
    }
}
