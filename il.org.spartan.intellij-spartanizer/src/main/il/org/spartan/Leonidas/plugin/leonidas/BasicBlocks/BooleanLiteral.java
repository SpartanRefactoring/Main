package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * A basic block representing "true" or "false".
 * @author Sharon KL & Michal Cohen & Oren Afek
 * @since 5/3/2017.
 */
public class BooleanLiteral extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "booleanLiteral";

    public BooleanLiteral(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected BooleanLiteral() {
        super(TEMPLATE);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return super.generalizes(e, m).combineWith(new MatchingResult(iz.booleanLiteral(e.getInner())));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return prev.getText().equals(next.getText());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new BooleanLiteral(e);
    }
}
