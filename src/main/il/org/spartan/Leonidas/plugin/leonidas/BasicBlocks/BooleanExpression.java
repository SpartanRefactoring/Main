package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.auxilary_layer.az;

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
    public boolean generalizes(Encapsulator e) {
        return super.generalizes(e) && az.expression(e.getInner()).getType() == PsiType.BOOLEAN;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new BooleanExpression(e);
    }
}
