package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericBlock extends GenericEncapsulator {
    public GenericBlock(PsiElement e) {
        super(e);
    }

    public GenericBlock(Encapsulator n) {
        super(n);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.blockStatement(e) || iz.block(e) || iz.statement(e);
    }
}
