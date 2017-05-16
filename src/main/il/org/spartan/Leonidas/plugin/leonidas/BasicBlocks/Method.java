package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class Method extends GenericMethodCallBasedBlock {
    public static final String TEMPLATE = "method";

    public Method(PsiElement e, String template) {
        super(e, template);
    }

    public Method(Encapsulator n, String template) {
        super(n, template);
    }

    public Method(Encapsulator e) {
        this(e, TEMPLATE);
    }

    public Method(PsiElement e) {
        this(e, TEMPLATE);
    }

    protected Method(String template) {
        super(template);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return iz.method(e.getInner());
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.method(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new Method(e);
    }

    /* Constraints Methods */

    public void startsWith(String ignore) {
    }
}
