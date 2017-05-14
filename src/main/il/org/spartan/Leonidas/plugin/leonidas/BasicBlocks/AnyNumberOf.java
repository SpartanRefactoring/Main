package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 14/05/2017
 */
public class AnyNumberOf extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "AnyNumberOf";
    Encapsulator internal;

    public AnyNumberOf(PsiElement e, Encapsulator i) {
        super(e, TEMPLATE);
        internal = i;
    }

    public AnyNumberOf() {
        super(TEMPLATE);
    }


    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.generic(internal) && az.generic(internal).goUpwards(prev, next);
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return null;
    }
}
