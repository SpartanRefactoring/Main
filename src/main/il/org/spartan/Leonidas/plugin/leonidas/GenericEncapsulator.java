package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.auxilary_layer.step;

import java.util.Collections;

/**
 * @author Oren Afek && Michal Cohen
 * @since 5/3/2017
 */
public abstract class GenericEncapsulator extends Encapsulator {

    protected String template;


    public GenericEncapsulator(PsiElement e, String template) {
        super(e);
        children = Collections.emptyList();
        this.template = template;
    }

    public GenericEncapsulator(Encapsulator n, String template) {
        super(n);
        children = Collections.emptyList();
        this.template = template;
    }


    /**
     * Can I generalize a PsiElement.
     *
     * @param e PSI Element
     * @return true iff I can generalize e
     */
    protected abstract boolean generalizes(PsiElement e);

    /**
     * Do I represent a concrete PsiElement
     *
     * @param other PSI Element
     * @return true iff I'm to be switched with other.
     */
    public abstract boolean conforms(PsiElement other);

    /**
     * Extracts the Id No. of the general block from the PsiElement
     * for example:
     * statement(0) -> 0
     *
     * @param e PsiElement
     * @return the id.
     */
    public abstract int extractId(PsiElement e);


    /**
     * Can I generalize a concrete element
     *
     * @param e concrete element
     * @return true iff I can generalize e
     */
    public boolean generalizes(Encapsulator e) {
        return generalizes(e.getInner());
    }

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
