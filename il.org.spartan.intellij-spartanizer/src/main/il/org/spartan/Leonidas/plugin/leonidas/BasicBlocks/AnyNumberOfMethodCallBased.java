package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * A base class for all quantifiers of type "AnyNumberOf" that are implemented using
 * method call expression. For example "anyNumberOf(Statement(3))".
 * @author Oren Afek
 * @since 14-05-2017
 */
public class AnyNumberOfMethodCallBased extends QuantifierMethodCallBased {

    private static final String TEMPLATE = "anyNumberOf";

    public AnyNumberOfMethodCallBased(PsiElement e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    public AnyNumberOfMethodCallBased() {
        super(TEMPLATE);
    }

    @Override
    public int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m) {
        if (!i.hasNext()) return 0;
        if (i.value().getParent() == null) return 1;
        Wrapper<Integer> count = new Wrapper<>(0);
        //noinspection StatementWithEmptyBody
        i.value().getParent().accept(n -> {
            if (generalizes(n, m).matches()) count.set(count.get() + 1);
        });
        return count.get();
    }

    @Override
    public AnyNumberOfMethodCallBased create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        step.firstParameterExpression(az.methodCallExpression(e.getInner()));
        return new AnyNumberOfMethodCallBased(e.getInner(), internalEncapsulator(e));
    }

    private Encapsulator internalEncapsulator(Encapsulator e) {
        return !e.getText().contains("anyNumberOf") ? e : e.getActualChildren().get(1).getActualChildren().get(1);
    }

    @Override
    public QuantifierIterator quantifierIterator(EncapsulatorIterator bgCursor, Map<Integer, List<PsiElement>> m) {
        return new QuantifierIterator(0, getNumberOfOccurrences(bgCursor, m));
    }
}
