package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericBooleanLiteral extends GenericEncapsulator {
    public GenericBooleanLiteral(Encapsulator n) {
        super(n);
    }

    public GenericBooleanLiteral(PsiElement e) {
        super(e);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.booleanLiteral(e);
    }


}
