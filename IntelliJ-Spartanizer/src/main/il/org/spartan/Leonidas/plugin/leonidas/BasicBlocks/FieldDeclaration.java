package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Sharon LK
 */
public class FieldDeclaration extends GenericMethodCallBasedBlock {
    public static final String TEMPLATE = "fieldDeclaration";

    public FieldDeclaration(PsiElement e) {
        super(e, TEMPLATE);
    }

    public FieldDeclaration(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected FieldDeclaration() {
        super(TEMPLATE);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.fieldDeclaration(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new FieldDeclaration(e);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return iz.fieldDeclaration(e.getInner());
    }
}
