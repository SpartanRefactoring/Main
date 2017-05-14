package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Encapsulating Psi elements so that non illegal transformation is done on the psi trees of Intellij.
 *
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
        this(n, n.parent);
    }

    public Encapsulator(Encapsulator n, Encapsulator parent) {
        this.parent = parent;
        inner = n.inner;
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
    public int getAmountOfActualChildren() {
        return getActualChildren().size();
    }

    private List<Encapsulator> getActualChildren() {
        return children.stream().filter(child -> !iz.whiteSpace(child.getInner()) && child.exists()).collect(Collectors.toList());
    }

    public String toString() {
        return inner != null ? inner.toString() : "stub";
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
    public Encapsulator generalizeWith(Encapsulator replacer) {
        parent.children.replaceAll(e -> e == Encapsulator.this ? replacer : e);
        replacer.parent = this.parent;
        return replacer;
    }

    public boolean isGeneric() {
        return false;
    }

    public boolean exists() {
        return true;
    }

    /**
     * <b>Linear Iterator</b> for iterating over the tree without considering white spaces.
     */
    public class Iterator implements java.util.Iterator<Encapsulator> {


        Encapsulator parent;
        Encapsulator current;
        Stack<IterationContext> states;
        private int location;

        public Iterator() {
            children = getActualChildren();
            states = new Stack<>();
        }

        @Override
        public boolean hasNext() {
            //TODO: @orenafek implement
            return true;
        }

        @Override
        public Encapsulator next() {
            return linearNext();
        }

        private Encapsulator linearNext() {
            assert hasNext();
            IterationContext context = states.peek();
            if (context.hasMoreChildren()) {
                current = context.nextChild();
            } else {
                states.pop();
                current = parent;
                parent = parent.getParent();

            }

            return null;
        }


        private Encapsulator goLeft() {
            return null;
        }

        public Encapsulator peekNext() {
            return children.get(location + 1);
        }

        public Encapsulator value() {
            return children.get(location);
        }

        private class IterationContext {
            int location;
            List<Encapsulator> children;

            public IterationContext(List<Encapsulator> children, int location) {
                this.location = location;
                this.children = children;
            }

            public Encapsulator nextChild() {
                return children.get(++location);
            }

            private boolean hasMoreChildren() {
                return location < children.size();
            }
        }
    }
}
