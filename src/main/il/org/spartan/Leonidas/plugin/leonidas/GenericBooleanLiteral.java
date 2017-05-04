package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Sharon KL & Michal Cohen & Oren Afek
 * @since 5/3/2017.
 */
public class GenericBooleanLiteral extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "booleanLiteral";

    public GenericBooleanLiteral(Encapsulator n) {
        super(n, TEMPLATE);
    }

    public GenericBooleanLiteral(PsiElement e) {
        super(e, TEMPLATE);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.booleanLiteral(e);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return prev.getText().equals(next.getText());
    }

    @Override
    public GenericEncapsulator create(PsiElement e) {
        return new GenericBooleanLiteral(e);
    }


}
