package il.org.spartan.Leonidas.plugin;

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
public class EncapsulatingNode implements Cloneable, VisitableNode, Iterable<EncapsulatingNode> {
    private PsiElement inner;
    private EncapsulatingNode parent;
    private List<EncapsulatingNode> children = new LinkedList<>();

    public EncapsulatingNode(PsiElement e) {
        inner = e;
        Arrays.stream(e.getChildren()).forEach(child -> children.add(new EncapsulatingNode(child, this)));
    }

    private EncapsulatingNode(PsiElement e, EncapsulatingNode parent) {
        this(e);
        this.parent = parent;
    }

    public EncapsulatingNode(EncapsulatingNode n) {
        this(n, null);
    }

    private EncapsulatingNode(EncapsulatingNode n, EncapsulatingNode parent) {
        this.parent = parent;
        inner = n.inner.copy();
        children = n.getChildren().stream().map(c -> new EncapsulatingNode(c, this)).collect(Collectors.toList());
    }

    /**
     * @param e PsiElement
     * @return an encapsulating node that hides e.
     */
    public static EncapsulatingNode buildTreeFromPsi(PsiElement e) {
        return new EncapsulatingNode(e);
    }

    /**
     * @param newNode the concrete node that replaces the generic node.
     * @param r       rewrite
     * @return this, for fluent API.
     */
    public EncapsulatingNode replace(EncapsulatingNode newNode, PsiRewrite r) {
        // this is bad, but better than using 'assert' like before because it can be tested
        if (!iz.generic(inner))
            throw new IllegalArgumentException();
        if (parent == null)
            return this;
        inner = r.replace(((GenericPsi) inner).getInner(), newNode.inner);
        return this;
    }

    public List<EncapsulatingNode> getChildren() {
        return children;
    }

    public EncapsulatingNode getParent() {
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

    public EncapsulatingNode clone() {
        return new EncapsulatingNode(this);
    }

    @Override
    public EncapsulatingNode.Iterator iterator() {
        return new EncapsulatingNode.Iterator();
    }

    @Override
    public void forEach(Consumer<? super EncapsulatingNode> action) {
        children.stream().forEach(action);
    }

    /**
     * Iterator for iterating over the tree without considering white spaces.
     */
    public class Iterator implements java.util.Iterator<EncapsulatingNode> {
        int location;
        List<EncapsulatingNode> noSpaceChildren;

        public Iterator() {
            noSpaceChildren = children.stream().filter(child -> !iz.whiteSpace(child.getInner())).collect(Collectors.toList());
        }

        @Override
        public boolean hasNext() {
            return location < noSpaceChildren.size();
        }

        @Override
        public EncapsulatingNode next() {
            EncapsulatingNode e = noSpaceChildren.get(location);
            ++location;
            return e;
        }

        public EncapsulatingNode value() {
            return noSpaceChildren.get(location);
        }


    }
}
