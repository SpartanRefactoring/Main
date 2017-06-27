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
 * A quantifier of type 'Optional' that is implemented by method call expression.
 * For example: "optional(statement(4))"
 * @author Oren Afek, michalcohen
 * @since 11-05-2017.
 */
public class OptionalMethodCallBased extends QuantifierMethodCallBased {
    private static final String TEMPLATE = "optional";

    public OptionalMethodCallBased(PsiElement e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    public OptionalMethodCallBased() {
        super(TEMPLATE);
    }

    @Override
    public OptionalMethodCallBased create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new OptionalMethodCallBased(e.getInner(), Pruning.prune(
				Encapsulator.buildTreeFromPsi(step.firstParameterExpression(az.methodCallExpression(e.getInner()))),
				m));
    }

    @Override
    public int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m) {
        return iz.conforms(i.value(), internal, m).matches() ? 1 : 0;
    }
}
