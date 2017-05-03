package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;

/**
 * @author Oren Afek && Michal Cohen
 * @since 5/3/2017
 */
public abstract class GenericEncapsulator extends Encapsulator {


    public GenericEncapsulator(PsiElement e) {
        super(e);
    }

    public GenericEncapsulator(Encapsulator n) {
        super(n);
    }

    public boolean generalizes(Encapsulator e){
        return generalizes(e.getInner());
    }

    protected abstract boolean generalizes(PsiElement e);


}
