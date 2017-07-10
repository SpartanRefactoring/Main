package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.auxilary_layer.iz;

import java.util.LinkedList;
import java.util.List;

/**
 * <b>Linear Eager EncapsulatorIterator</b> for iterating over the tree without considering white spaces.
 * This iterator can hold its advance for n {@link #next() next} calls by calling
 * {@link #setNumberOfOccurrences(int) setNoOfOccurrences(int) }.
 *
 * @author Oren Afek
 * @since 14/05/17
 */
public class EncapsulatorIterator implements java.util.Iterator<Encapsulator>, Cloneable {

    int cursor;
    private int skipCounter;
    private int skipOverall;
    private boolean shouldSkip;
    private List<Encapsulator> elements = new LinkedList<>(); // preorder traversal on the forest

    public EncapsulatorIterator(Encapsulator e) {
        initializeElements(e, elements);
    }

    public EncapsulatorIterator(List<Encapsulator> roots) {
        roots.forEach(root -> initializeElements(root, elements));
    }

    private void initializeElements(Encapsulator e, List<Encapsulator> es) {
        if (!iz.whiteSpace(e.getInner()) && !iz.comment(e.getInner())) es.add(e);
        e.getActualChildren().forEach(c -> initializeElements(c, es));
    }

    @Override
    public boolean hasNext() {
        return shouldSkip || cursor < elements.size();
    }

    @Override
    public Encapsulator next() {
        if (shouldSkip && skipCounter < skipOverall) {
            ++skipCounter;
            return elements.get(cursor);
        }
        shouldSkip = false;
        return elements.get(cursor++);
    }

    public Encapsulator value() {
        return elements.get(cursor);
    }

    @Override
    public EncapsulatorIterator clone() {
        try {
            EncapsulatorIterator cloned = (EncapsulatorIterator) super.clone();
            cloned.elements = new LinkedList<>(elements);
            cloned.cursor = cursor;
            cloned.shouldSkip = shouldSkip;
            cloned.skipCounter = skipCounter;
            cloned.skipOverall = skipOverall;
            return cloned;
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    public int getNumberOfOccurrences() {
        return skipOverall;
    }

    public EncapsulatorIterator setNumberOfOccurrences(int i) {
        shouldSkip = true;
        skipOverall = i;
        return this;
    }

    /**
     * Deletes from the list of elements all the elements that were already matched by a higher level composite
     * component, matched to a generic encapsulator.
     */
    public void matchedWithGeneric() {
        Encapsulator current = elements.get(cursor);
        current.accept(n -> {
            if (n != current)
                elements.remove(n);
        });
    }

    @Override
    public String toString() {
        return hasNext() ? value().toString() + "\n\nHas next? " + hasNext() : "ended";
    }
}