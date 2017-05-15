package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 14/05/2017
 */
public class AnyNumberOf extends Quantifier {

    private static final String TEMPLATE = "AnyNumberOf";
    Encapsulator internal;

    public AnyNumberOf(PsiElement e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    public AnyNumberOf() {
        super(TEMPLATE);
    }


    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.generic(internal) && az.generic(internal).goUpwards(prev, next);
    }

    @Override
    public int getNumberOfOccurrences(Encapsulator.Iterator i) {
        int s = 0;
        for (Encapsulator.Iterator bgCursor = i.clone(); conforms(bgCursor.value().getInner()); bgCursor.next(), s++)
            ;
        return s;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return null;
    }
}
