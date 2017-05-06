package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Encapsulating Psi elements so that non illegal transformation is done on the psi trees of Intellij.
 * @author michalcohen
 * @since 22-02-2017
 */
public class Encapsulator implements Cloneable, VisitableNode, Iterable<Encapsulator> {
    protected PsiElement inner;
    protected Encapsulator parent;
    protected List<Encapsulator> children = new LinkedList<>();

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Encapsulator() {
    }

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

    public Encapsulator(Encapsulator n, Encapsulator parent) {
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



    public List<Encapsulator> getChildren() {
        return children;
    }

    public Encapsulator getParent() {
        return parent;
    }

    @Override
    public void accept(EncapsulatorVisitor v) {
        v.visit(this);
        children.forEach(child -> child.accept(v));
    }

    @Override
    public <T> T accept(EncapsulatorValueVisitor<T> v, BinaryOperator<T> accumulator) {
        return accumulator.apply(v.visit(this), children.stream().filter(Objects::nonNull).map(child -> child.accept(v, accumulator))
                .reduce(accumulator).orElse(null));
    }

    public PsiElement getInner() {
        return inner;
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

    @NotNull
    public String getText() {
        return inner.getText() != null ? inner.getText() : "";
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Encapsulator clone() {
        return new Encapsulator(this);
    }

    @Override
    public Encapsulator.Iterator iterator() {
        return new Encapsulator.Iterator();
    }

    @Override
    public void forEach(Consumer<? super Encapsulator> action) {
        children.forEach(action);
    }


    public <T> void putUserData(Key<T> key, T id) {
        this.inner.putUserData(key, id);
    }

    public <T> T getUserData(Key<T> id) {
        return inner.getUserData(id);
    }

    /**
     * Replaces a concrete element with a generalized one.
     *
     * @param replacer the new Generalized element
     * @return the replacer
     */
    public Encapsulator generalize(Encapsulator replacer) {
        parent.children.replaceAll(e -> e == Encapsulator.this ? replacer : e);
        replacer.parent = this.parent;
        return replacer;
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
