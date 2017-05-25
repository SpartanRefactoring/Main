package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Oren Afek && Michal Cohen
 * @since 03-05-2017
 */
public abstract class GenericEncapsulator extends Encapsulator {
    private List<Constraint> constraints = new ArrayList<>();
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
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected GenericEncapsulator() {
    }

    /**
     * Does the given PsiElement is a stub representing the general form?
     *
     * @param other PSI Element
     * @return true iff other needs to be switched with this generic component.
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
    public abstract Integer extractId(PsiElement e);

    /**
     * Prunes the irrelevant concrete PsiElements and replaces with a new Generic Encapsulator
     *
     * @param e the concrete PsiElement.
     *          for example: the methodCallExpression: statement(0).
     * @return The replacer of the syntactic generic element with the GenericEncapsulator
     */
    public Encapsulator prune(Encapsulator e) {
        assert conforms(e.getInner());
        Encapsulator upperElement = getConcreteParent(e);
        GenericEncapsulator ge = create(upperElement);
        if (isGeneric())
            ge.putId(ge.extractId(e.getInner()));
        return ge.getParent() == null ? ge : upperElement.generalizeWith(ge);
    }

    protected abstract boolean goUpwards(Encapsulator prev, Encapsulator next);

    /**
     * Creates another one like me, with concrete PsiElement within
     *
     * @param e element within.
     * @return new <B>Specific</B> GenericEncapsulator
     */
    public abstract GenericEncapsulator create(Encapsulator e);

    /**
     * Can I generalize with a concrete element
     *
     * @param e concrete element
     * @return true iff I can generalize with e
     */
    @SuppressWarnings("InfiniteRecursion")
    public boolean generalizes(Encapsulator e) {
        // Check if there is any constraint that cannot be accepted with the given element
        return constraints.stream()
                .filter(c -> !c.accept(e))
                .count() == 0;
    }

    public void replaceByRange(List<PsiElement> elements, PsiRewrite r) {
        assert parent != null;
        List<PsiElement> l = Lists.reverse(elements);
        elements.forEach(e -> inner.getParent().addAfter(e, inner));
        inner.getParent().deleteChildRange(inner, inner);
    }

    /**
     * @param n method call representing generic element.
     * @return the highest generic parent.
     */
    public Encapsulator getConcreteParent(Encapsulator n) {
        if (n.parent == null) return n;
        Encapsulator prev = n, next = n.getParent();
        while (goUpwards(prev, next)) {
            prev = next;
            next = next.getParent();
        }
        return prev;
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    protected void addConstraint(Constraint c) {
        constraints.add(c);
    }

    protected interface Constraint {
        boolean accept(Encapsulator encapsulator);
    }
}
