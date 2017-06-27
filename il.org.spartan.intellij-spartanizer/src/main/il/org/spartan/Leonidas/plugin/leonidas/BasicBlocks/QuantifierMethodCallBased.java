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
 * A base class for all quantifiers that are represented by a method call expression.
 * @author Oren Afek
 * @since 14-05-2017.
 */
public abstract class QuantifierMethodCallBased extends GenericMethodCallBasedBlock {

    protected Encapsulator internal;

    public QuantifierMethodCallBased(PsiElement e, String template, Encapsulator i) {
        super(e, template);
        internal = i;
    }

    public QuantifierMethodCallBased(String template) {
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
        return Toolbox.getInstance().getGeneric(ie).map(λ -> λ.extractId(ie)).orElse(null);
    }

    @Override
    public Encapsulator prune(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        assert conforms(e.getInner());
        QuantifierMethodCallBased o = create(e, map);
        Encapsulator upperElement = o.getConcreteParent(e);
        o.inner = upperElement.inner;
        if (!o.isGeneric())
			return upperElement.getParent() == null ? o : upperElement.generalizeWith(o);
		o.putId(o.extractId(e.getInner()));
		o.extractAndAssignDescription(e.getInner());
		return upperElement.getParent() == null ? o : upperElement.generalizeWith(o);
    }

    public Encapsulator getConcreteParent(Encapsulator n,  Map<Integer, List<Matcher.Constraint>> map) {
        return create(n, map).getConcreteParent(n);
    }

    @PreservesIterator
    public abstract int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m);

    public abstract QuantifierMethodCallBased create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map);

    public Encapsulator getInternal(){
        return internal;
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> elements, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        if (!iz.generic(internal)) return super.replaceByRange(elements, m ,r);
        elements = az.generic(internal).applyReplacingRules(elements, m);
        if (parent == null) return elements;
        List<PsiElement> l = Lists.reverse(elements);
        l.forEach(λ -> r.addAfter(inner.getParent(), inner, λ));
        r.deleteByRange(inner.getParent(), inner, inner);
        return elements;
    }
}
