package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.auxilary_layer.iz;

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

    @Override
    public boolean generalizes(Encapsulator e) {
        return super.generalizes(e);
    }

    /* Constraints */

    /**
     * Will accept only literal expressions.
     */
    public void mustBeLiteral() {
        addConstraint(e -> iz.literal(e.inner));
    }
}
