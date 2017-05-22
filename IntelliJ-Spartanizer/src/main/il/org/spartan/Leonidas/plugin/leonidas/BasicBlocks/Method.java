package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class Method extends GenericMethodCallBasedBlock {
    public static final String TEMPLATE = "method";

    public Method(Encapsulator e) {
        super(e, TEMPLATE);
    }

    public Method() {
        super(TEMPLATE);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return iz.method(e.getInner());
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
