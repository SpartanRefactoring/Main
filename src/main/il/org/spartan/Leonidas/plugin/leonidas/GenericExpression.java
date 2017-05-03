package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;

import java.util.Objects;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericExpression extends GenericEncapsulator {

    PsiType type;

    public GenericExpression(PsiElement e, PsiType type) {
        super(e);
        this.type = type;
    }

    public GenericExpression(Encapsulator n) {
        super(n);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.expression(e);
    }

    public PsiType evaluationType() {
        return type;
    }
}
