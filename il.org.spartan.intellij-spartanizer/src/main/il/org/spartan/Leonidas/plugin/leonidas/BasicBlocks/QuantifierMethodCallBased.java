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
public abstract class QuantifierMethodCallBased extends GenericMethodCallBasedBlock implements Quantifier {

    protected Encapsulator internal;

    public QuantifierMethodCallBased(PsiElement e, String template, Encapsulator i) {
        super(e, template);
        internal = i;
    }

    public QuantifierMethodCallBased(String template) {
        super(template);
    }

    @Override
    public boolean goUpwards(Encapsulator prev, Encapsulator next) {
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
    public Encapsulator prune(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        assert conforms(e.getInner());
        QuantifierMethodCallBased o = create(e, m);
        Encapsulator upperElement = o.getConcreteParent(e);
        o.inner = upperElement.inner;
        if (!o.isGeneric())
            return upperElement.getParent() == null ? o : upperElement.generalizeWith(o);
        o.putId(o.extractId(e.getInner()));
        o.extractAndAssignDescription(e.getInner());
        return upperElement.getParent() == null ? o : upperElement.generalizeWith(o);
    }

    public Encapsulator getConcreteParent(Encapsulator e,  Map<Integer, List<Matcher.Constraint>> m) {
        return create(e, m).getConcreteParent(e);
    }

    @PreservesIterator
    public abstract int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m);

    public abstract QuantifierMethodCallBased create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m);

    public Encapsulator getInternal(){
        return internal;
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> es, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        if (!iz.generic(internal)) return super.replaceByRange(es, m, r);
        GenericEncapsulator ge = az.generic(internal);
        if (es == null) {
            r.deleteByRange(inner.getParent(), inner, inner);
            return null;
        }
        es = ge.applyReplacingRules(es, m);
        if (parent == null) return es;
        List<PsiElement> l = Lists.reverse(es);
        l.forEach(e -> r.addAfter(inner.getParent(), inner, e));
        r.deleteByRange(inner.getParent(), inner, inner);
        return es;
    }
}
