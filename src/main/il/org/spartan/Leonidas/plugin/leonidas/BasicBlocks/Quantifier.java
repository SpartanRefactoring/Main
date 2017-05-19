package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.leonidas.PreservesIterator;

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

    public Quantifier(String template) {
        super(template);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.generic(internal) && az.generic(internal).goUpwards(prev, next);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return (internal != null && iz.generic(internal)) && az.generic(internal).generalizes(e);
    }

    @Override
    public int extractId(PsiElement e) {
        PsiElement ie = step.firstParameterExpression(az.methodCallExpression(e));
        return Toolbox.getInstance().getGeneric(ie).map(g -> g.extractId(ie)).orElse(null);
    }

    public Encapsulator prune(Encapsulator e) {
        assert conforms(e.getInner());
        Quantifier o = create(e);
        Encapsulator upperElement = o.getConcreteParent(e);
        o.inner = upperElement.inner;
        if (o.isGeneric())
            upperElement.putId(o.extractId(e.getInner()));//o
        return upperElement.getParent() == null ? upperElement : upperElement.generalizeWith(o);
    }

    @PreservesIterator
    public abstract int getNumberOfOccurrences(Encapsulator.Iterator i);

    public abstract Quantifier create(Encapsulator e);

}
