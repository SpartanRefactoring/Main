package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.iz;

import java.util.Collections;

/**
 * @author Oren Afek && Michal Cohen
 * @since 5/3/2017
 */
public abstract class GenericEncapsulator extends Encapsulator {


    public GenericEncapsulator(PsiElement e) {
        super(e);
        children = Collections.emptyList();
    }

    public GenericEncapsulator(Encapsulator n) {
        super(n);
        children = Collections.emptyList();
    }

    public boolean generalizes(Encapsulator e){
        return generalizes(e.getInner());
    }

    protected abstract boolean generalizes(PsiElement e);

    /**
     * @param newNode the concrete node that replaces the generic node.
     * @param r       rewrite
     * @return this, for fluent API.
     */
    public Encapsulator replace(Encapsulator newNode, PsiRewrite r) {
        inner = parent == null ? newNode.inner : r.replace(inner, newNode.inner);
        return this;
    }

}
