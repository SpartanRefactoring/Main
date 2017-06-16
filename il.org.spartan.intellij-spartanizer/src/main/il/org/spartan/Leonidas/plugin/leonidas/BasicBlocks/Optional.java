package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;

import java.util.List;
import java.util.Map;

/**
 * @author Oren Afek, michalcohen
 * @since 11-05-2017.
 */
public class Optional extends Quantifier {
    private static final String TEMPLATE = "optional";

    public Optional(PsiElement e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    public Optional() {
        super(TEMPLATE);
    }

    @Override
    public Optional create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        PsiElement p = step.firstParameterExpression(az.methodCallExpression(e.getInner()));
        return new Optional(e.getInner(), Pruning.prune(Encapsulator.buildTreeFromPsi(p), map));
    }

    @Override
    public int getNumberOfOccurrences(EncapsulatorIterator it, Map<Integer, List<PsiElement>> m) {
        return iz.conforms(it.value(), internal, m).matches() ? 1 : 0;
    }

}
