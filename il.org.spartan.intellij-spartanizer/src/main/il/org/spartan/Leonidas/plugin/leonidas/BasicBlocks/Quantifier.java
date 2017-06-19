package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;
import il.org.spartan.Leonidas.plugin.leonidas.PreservesIterator;

import java.util.List;
import java.util.Map;

/**
 * @author Oren Afek
 * @since 14-05-2017.
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
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return new MatchingResult(internal != null && iz.generic(internal) && az.generic(internal).generalizes(e, m).matches());

    }

    @Override
    public Integer extractId(PsiElement e) {
        PsiElement ie = step.firstParameterExpression(az.methodCallExpression(e));
        return Toolbox.getInstance().getGeneric(ie).map(g -> g.extractId(ie)).orElse(null);
    }

    @Override
    public Encapsulator prune(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        assert conforms(e.getInner());
        Quantifier o = create(e, map);
        Encapsulator upperElement = o.getConcreteParent(e);
        o.inner = upperElement.inner;
        if (o.isGeneric())
            /*upperElement.putId(o.extractId(e.getInner()));*/
            o.putId(o.extractId(e.getInner()));
        return upperElement.getParent() == null ? o : upperElement.generalizeWith(o);
    }

    @PreservesIterator
    public abstract int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m);

    public abstract Quantifier create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map);

    public Encapsulator getInternal(){
        return internal;
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> elements, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        if (!iz.generic(internal)) return super.replaceByRange(elements, m ,r);
        GenericEncapsulator ge = az.generic(internal);
        elements = ge.applyReplacingRules(elements, m);
        if (parent == null) return elements;
        List<PsiElement> l = Lists.reverse(elements);
        l.forEach(e -> r.addAfter(inner.getParent(), inner, e));
        r.deleteByRange(inner.getParent(), inner, inner);
        return elements;
    }
}
