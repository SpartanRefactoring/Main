package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class Statement extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "statement";

    public Statement(PsiElement e) {
        super(e, TEMPLATE);
    }

    public Statement(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Statement() {
        super(TEMPLATE);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e) {
        return new MatchingResult(iz.statement(e.getInner()) && !iz.blockStatement(e.getInner()));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return prev.getText().equals(next.getText()) || next.getText().equals(prev.getText() + ";");
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Statement(e);
    }


}
