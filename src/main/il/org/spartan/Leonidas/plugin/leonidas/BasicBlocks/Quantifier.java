package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;

/**
 * @author Oren Afek
 * @since 5/14/2017.
 */
public abstract class Quantifier extends GenericMethodCallBasedBlock {

    protected Encapsulator internal;

    public Quantifier(PsiElement e, String template, Encapsulator i) {
        super(e, template);
        internal = i;
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.generic(internal) && az.generic(internal).goUpwards(prev, next);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return iz.conforms(internal, e);
    }

    @Override
    public int extractId(PsiElement e) {
        assert iz.generic(internal);
        return az.generic(internal).extractId(step.firstParameterExpression(az.methodCallExpression(e)));
    }

    @Override
    public boolean isGeneric() {
        return internal.isGeneric();
    }


}
