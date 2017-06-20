package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import org.apache.commons.lang.math.RandomUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author RoeiRaz
 * @since 18/06/17
 * <p>
 * SpartanizationBatch is a collection of spartanization work instructions
 * that can be invoked in the future. Should LOCK the given PSI element.
 */
public class SpartanizationBatch {

    private static final String WRITE_COMMAND_NAME = SpartanizationBatch.class.getSimpleName()
            + RandomUtils.nextInt();

    private final Project project;
    private final Set<PsiElement> elements;
    private final Set<PsiFile> files;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<SpartanizerBatchListener> listenerOptional;
    private final WriteCommandAction commandAction;

    SpartanizationBatch(Project project, Set<? extends PsiElement> elements) {
        this(project, elements, null);
    }

    SpartanizationBatch(Project project, Set<? extends PsiElement> elements, SpartanizerBatchListener listener) {
        this.project = project;
        this.elements = new HashSet<>(elements);
        this.listenerOptional = Optional.ofNullable(listener);
        this.files = elements.stream().map(PsiElement::getContainingFile).collect(Collectors.toSet());
        this.commandAction = new RunCommand(project, WRITE_COMMAND_NAME, files.stream().toArray(PsiFile[]::new));

    }

    public void invoke() {
        commandAction.execute();
    }

    public Project getProject() {
        return project;
    }

    public Set<PsiElement> getElements() {
        return elements;
    }

    private class RunCommand extends WriteCommandAction.Simple {
        RunCommand(Project project, String commandName, PsiFile... files) {
            super(project, commandName, files);
        }

        @Override
        protected void run() throws Throwable {
            getElements().forEach(e -> {
                if (Toolbox.getInstance().checkExcluded(e.getContainingFile()))
                    return;
                e.accept(new JavaRecursiveElementVisitor() {
                    @Override
                    public void visitElement(PsiElement ce) {
                        super.visitElement(ce);
                        if (!Toolbox.getInstance().canTip(ce))
                            return;
                        Toolbox.getInstance().getAllTippers().forEach(t -> {
                            if (!t.canTip(ce))
                                return;
                            //noinspection unchecked
                            t.tip(ce).go(new PsiRewrite().project(project).psiFile(e.getContainingFile()));
                        });
                    }
                });

            });
        }
    }
}
