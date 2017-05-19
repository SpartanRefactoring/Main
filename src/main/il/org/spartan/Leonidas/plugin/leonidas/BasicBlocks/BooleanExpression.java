package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiType;

/**
 * @author michalcohen
 * @since 08-05-2017.
 */
public class BooleanExpression extends Expression {
    private static final String TEMPLATE = "booleanExpression";

    public BooleanExpression(Encapsulator e) {
        super(e, TEMPLATE, PsiType.BOOLEAN);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public BooleanExpression() {
        super(TEMPLATE);
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new BooleanExpression(e);
    }
}
