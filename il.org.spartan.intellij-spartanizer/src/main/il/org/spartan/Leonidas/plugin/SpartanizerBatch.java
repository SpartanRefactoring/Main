package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

/**
 * @author RoeiRaz
 * @since 18/06/17
 *
 * SpartanizerBatch is a collection of spartanization work instructions
 * that can be invoked in the future. There is no guarantee that the instructions contained
 * will be valid when the batch is invoked. For example, it may contain a deleted element.
 * The invocation however, must be valid and pass without errors.
 */
public class SpartanizerBatch {

    private final Project project;
    private final List<PsiElement> elements;
    private final Optional<SpartanizerBatchListener> listenerOptional;

    SpartanizerBatch(Project project, List<? extends PsiElement> elements) {
        this(project, elements, null);
    }

    SpartanizerBatch(Project project, List<? extends PsiElement> elements, SpartanizerBatchListener listener) {
        this.project = project;
        this.elements = new ArrayList<>(elements);
        this.listenerOptional = Optional.ofNullable(listener);
    }

    public void invoke() {
        listenerOptional.ifPresent(l -> l.onInvoke(this));
        elements.stream().forEach(this::perform);
    }

    public Project getProject() {
        return project;
    }

    public List<PsiElement> getElements() {
        return elements;
    }

    /**
     * Performs spartanization on element only if it part of the project.
     * Reports error otherwise.
     * @param element
     */
    @SuppressWarnings("unchecked")
    private void perform(PsiElement element) {
        // TODO Crashing the software - BAD! Logging - GOOD!
        if (! element.getProject().equals(project))
            throw new RuntimeException("illegal element encountered while performing batch spartanization");
        Spartanizer.spartanizeRecursively(element);
        // TODO the current (UGLY!!) 'Spartanizer' class needs a major update before this class can be written properly. @Team8
        listenerOptional.ifPresent(l -> l.onTip(this, element, null));
    }
}
