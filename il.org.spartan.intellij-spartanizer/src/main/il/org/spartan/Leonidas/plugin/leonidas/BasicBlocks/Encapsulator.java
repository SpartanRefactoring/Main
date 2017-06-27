package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters.ID;

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
        PsiRewrite r = new PsiRewrite();
        Arrays.stream(e.getChildren()).filter(child -> iz.comment(child.getLastChild()) && az.comment(child.getLastChild()).getText().contains("ignore")).forEach(child -> r.delete(child));
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

    /**
     * @param e PsiElement
     * @return an encapsulating node that hides e.
     */
    public static Encapsulator buildTreeFromPsi(PsiElement e, PsiElement parent) {
        return new Encapsulator(e, new Encapsulator(parent));
    }



    public List<Encapsulator> getChildren() {
        return children;
    }

    public Encapsulator getParent() {
        return parent;
    }

    @Override
    public void accept(EncapsulatorVisitor v) {
        children.forEach(child -> child.accept(v));
        v.visit(this);
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

    public List<Encapsulator> getActualChildren() {
        return children.stream().filter(child -> !iz.whiteSpace(child.getInner())).collect(Collectors.toList());
    }

    public String toString() {
        return inner == null ? "stub" : inner + "";
    }

    @NotNull
    public String getText() {
        return inner.getText() == null ? "" : inner.getText();
    }

    @Override
    public EncapsulatorIterator iterator() {
        return new EncapsulatorIterator(this);
    }

    /**
     * Replaces a concrete element with a generalized one.
     *
     * @param replacer the new Generalized element
     * @return the replacer
     */
    public Encapsulator generalizeWith(Encapsulator replacer) {
        parent.children.replaceAll(e -> e != Encapsulator.this ? e : replacer);
        replacer.parent = this.parent;
        return replacer;
    }

    public boolean isGeneric() {
        return false;
    }

    /**
     * @return the ID of the generic element
     */
    public Integer getId() {
        return inner.getUserData(ID);
    }

    /**
     * @param i ID
     * @return this, for fluent API
     */
    public Encapsulator putId(Integer i) {
        inner.putUserData(ID, i);
        return this;
    }


}
