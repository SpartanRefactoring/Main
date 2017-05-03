package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericStatement extends GenericEncapsulator {

    public GenericStatement(PsiElement e) {
        super(e);
    }

    public GenericStatement(Encapsulator n) {
        super(n);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.statement(e) && !iz.blockStatement(e);
    }
}
