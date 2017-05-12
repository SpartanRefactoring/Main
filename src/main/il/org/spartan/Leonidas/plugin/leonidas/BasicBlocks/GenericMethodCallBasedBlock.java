package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;

/**
 * @author Oren Afek & Michal Cohen
 * @since 5/4/2017.
 */
public abstract class GenericMethodCallBasedBlock extends GenericEncapsulator {

    public GenericMethodCallBasedBlock(PsiElement e, String template) {
        super(e, template);
    }

    public GenericMethodCallBasedBlock(Encapsulator n, String template) {
        super(n, template);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected GenericMethodCallBasedBlock(String template) {
        super();
        this.template = template;
    }

    @Override
    public boolean conforms(PsiElement other) {
        return iz.methodCallExpression(other) &&
                step.methodExpression(az.methodCallExpression(other)).getText()
                        .equals(template);
    }

    @Override
    public int extractId(PsiElement e) {
        assert (conforms(e));
        return az.integer(step.firstParameterExpression(az.methodCallExpression(e)));
    }
}
