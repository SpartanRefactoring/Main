package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes.GenericPsi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Encapsulating Psi elements so that non illegal transformation is done on the psi trees of Intellij.
 * @author michalcohen
 * @since 22-02-2017
 */
public class Encapsulator implements Cloneable, VisitableNode, Iterable<Encapsulator> {
    private PsiElement inner;
    private Encapsulator parent;
    private List<Encapsulator> children = new LinkedList<>();

    public Encapsulator(PsiElement e) {
        inner = e;
        Arrays.stream(e.getChildren()).forEach(child -> children.add(new Encapsulator(child, this)));
    }

    private Encapsulator(PsiElement e, Encapsulator parent) {
        this(e);
        this.parent = parent;
    }

    public Encapsulator(Encapsulator n) {
        this(n, null);
    }

    private Encapsulator(Encapsulator n, Encapsulator parent) {
        this.parent = parent;
        inner = n.inner.copy();
        children = n.getChildren().stream().map(c -> new Encapsulator(c, this)).collect(Collectors.toList());
    }

    /**
     * @param e PsiElement
     * @return an encapsulating node that hides e.
     */
    public static Encapsulator buildTreeFromPsi(PsiElement e) {
        return new Encapsulator(e);
    }

    /**
     * @param newNode the concrete node that replaces the generic node.
     * @param r       rewrite
     * @return this, for fluent API.
     */
    public Encapsulator replace(Encapsulator newNode, PsiRewrite r) {
        if (!iz.generic(inner))
            throw new IllegalArgumentException();
        if (parent == null) {
            inner = newNode.inner;
            return this;
        }
        inner = r.replace(((GenericPsi) inner).getInner(), newNode.inner);
        return this;
    }

    public List<Encapsulator> getChildren() {
        return children;
    }

    public Encapsulator getParent() {
        return parent;
    }

    @Override
    public void accept(EncapsulatingNodeVisitor v) {
        v.visit(this);
        children.forEach(child -> child.accept(v));
    }

    @Override
    public <T> T accept(EncapsulatingNodeValueVisitor<T> v, BinaryOperator<T> accumulator) {
        return accumulator.apply(v.visit(this), children.stream().filter(child -> child != null).map(child -> child.accept(v, accumulator))
                .reduce(accumulator).orElse(null));
    }

    public PsiElement getInner() {
        return inner;
    }

    public void setInner(GenericPsi inner) {
        this.inner = inner;
        children = new LinkedList<>();
    }

    /**
     * @return the amount of children that are not white space Psi elements.
     */
    public int getAmountOfNoneWhiteSpaceChildren() {
        return children.stream().filter(child -> !iz.whiteSpace(child.getInner())).collect(Collectors.toList()).size();
    }

    public String toString() {
        return inner.toString();
    }

    public String getText() {
        return inner.getText();
    }

    public Encapsulator clone() {
        return new Encapsulator(this);
    }

    @Override
    public Encapsulator.Iterator iterator() {
        return new Encapsulator.Iterator();
    }

    @Override
    public void forEach(Consumer<? super Encapsulator> action) {
        children.stream().forEach(action);
    }

    /**
     * Iterator for iterating over the tree without considering white spaces.
     */
    public class Iterator implements java.util.Iterator<Encapsulator> {
        int location;
        List<Encapsulator> noSpaceChildren;

        public Iterator() {
            noSpaceChildren = children.stream().filter(child -> !iz.whiteSpace(child.getInner())).collect(Collectors.toList());
        }

        @Override
        public boolean hasNext() {
            return location < noSpaceChildren.size();
        }

        @Override
        public Encapsulator next() {
            Encapsulator e = noSpaceChildren.get(location);
            ++location;
            return e;
        }

        public Encapsulator value() {
            return noSpaceChildren.get(location);
        }


    }
}
