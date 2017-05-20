package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;

/**
 * @author Oren Afek
 * @since 14/05/2017
 */
public class AnyNumberOf extends Quantifier {

    private static final String TEMPLATE = "anyNumberOf";
    Encapsulator internal;

    public AnyNumberOf(PsiElement e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    public AnyNumberOf() {
        super(TEMPLATE);
    }

    @Override
    public int getNumberOfOccurrences(Encapsulator.Iterator i) {
        if (i.value().getParent() == null) return 1;
        Wrapper<Integer> count = new Wrapper<>(0);
        //noinspection StatementWithEmptyBody
        i.value().getParent().accept(new EncapsulatorVisitor() {
            @Override
            public void visit(Encapsulator n) {
                if (generalizes(n)) count.set(count.get() + 1);
            }
        });
        return count.get();
    }

    @Override
    public AnyNumberOf create(Encapsulator e) {
        PsiElement p = step.firstParameterExpression(az.methodCallExpression(e.getInner()));
        return new AnyNumberOf(e.getInner(), Pruning.prune(Encapsulator.buildTreeFromPsi(p)));
    }
}
