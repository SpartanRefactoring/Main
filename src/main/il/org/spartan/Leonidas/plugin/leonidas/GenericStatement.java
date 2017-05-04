package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericStatement extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "statement";

    public GenericStatement(PsiElement e) {
        super(e, TEMPLATE);
    }

    public GenericStatement(Encapsulator n) {
        super(n, TEMPLATE);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.statement(e) && !iz.blockStatement(e);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return prev.getText().equals(next.getText()) || next.getText().equals(prev.getText() + ";");
    }

    @Override
    public GenericEncapsulator create(PsiElement e) {
        return new GenericStatement(e);
    }


}
