package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericExpression extends GenericEncapsulator {

    public GenericExpression(PsiElement e) {
        super(e);
    }

    public GenericExpression(Encapsulator n) {
        super(n);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.expression(e);
    }
}
