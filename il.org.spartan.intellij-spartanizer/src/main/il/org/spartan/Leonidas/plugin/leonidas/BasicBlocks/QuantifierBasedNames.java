package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;
import il.org.spartan.Leonidas.plugin.leonidas.PreservesIterator;

import java.util.List;
import java.util.Map;

/**
 * A basic class for all quantifiers that are represented by their names.
 * @author Oren Afek, michalcohen
 * @since 20-06-2017.
 */
public abstract class QuantifierBasedNames extends NamedElement{
    protected Encapsulator internal;

    public QuantifierBasedNames(Encapsulator e, String template, Encapsulator i) {
        super(e, template);
        internal = i;
    }

    public QuantifierBasedNames(String template) {
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
    public Encapsulator prune(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        assert conforms(e.getInner());
        QuantifierBasedNames $ = create(e, m);
        Encapsulator upperElement = $.getConcreteParent(e);
        $.inner = upperElement.inner;
        if ($.isGeneric())
            $.putId($.extractId(e.getInner()));
        return upperElement.getParent() == null ? $ : upperElement.generalizeWith($);
    }

    @PreservesIterator
    public abstract int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m);

    public abstract QuantifierBasedNames create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m);

    public Encapsulator getInternal(){
        return internal;
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> $, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        if (!iz.generic(internal)) return super.replaceByRange($, m ,r);
        $ = az.generic(internal).applyReplacingRules($, m);
        if (parent == null) return $;
        List<PsiElement> l = Lists.reverse($);
        l.forEach(λ -> r.addAfter(inner.getParent(), inner, λ));
        r.deleteByRange(inner.getParent(), inner, inner);
        return $;
    }
}