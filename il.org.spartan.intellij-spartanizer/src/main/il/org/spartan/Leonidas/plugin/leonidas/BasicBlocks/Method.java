package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class Method extends GenericEncapsulator {
    public static final String TEMPLATE = "method";

    public Method(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Method() {
        super();
    }

    @Override
    public boolean conforms(PsiElement other) {
        return iz.method(other) && az.method(other).getName().startsWith(TEMPLATE);
    }

    @Override
    public Integer extractId(PsiElement e) {
        return Integer.parseInt(az.method(e).getName().substring(TEMPLATE.length()));
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return super.generalizes(e) && iz.method(e.getInner());
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return next != null && iz.method(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new Method(e);
    }

    /* Constraints Methods */

    public void startsWith(String s) {
        // TODO
    }
}
