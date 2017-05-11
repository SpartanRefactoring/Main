package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

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
    protected boolean generalizes(PsiElement e) {
        return iz.blockStatement(e) || iz.block(e) || iz.statement(e);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return !iz.block(prev.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new Block(e);
    }
}
