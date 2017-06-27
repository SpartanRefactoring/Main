package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * @author Roey Maor & Amir Sagiv & Michal Cohen & Oren Afek
 * @since 5/3/2017.
 */
public class Block extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "anyBlock";

    public Block(PsiElement e) {
        super(e, TEMPLATE);
    }

    public Block(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Block() {
        super(TEMPLATE);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return super.generalizes(e, m).combineWith(new MatchingResult(iz.blockStatement(e.getInner()) || iz.block(e.getInner()) || iz.statement(e.getInner())));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return !iz.block(prev.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new Block(e);
    }
}
