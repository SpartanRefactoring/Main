package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class GenericExpression extends GenericMethodCallBasedBlock {

    //FIXME @michalcohen @orenafek change to expression
    private static final String TEMPLATE = "booleanExpression";
    PsiType type;

    public GenericExpression(PsiElement e, PsiType type) {
        super(e, TEMPLATE);
        this.type = type;
    }

    public GenericExpression(PsiElement e) {
        super(e, TEMPLATE);
    }

    public GenericExpression(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected GenericExpression() {
        super(TEMPLATE);
    }

    @Override
    protected boolean generalizes(PsiElement e) {
        return iz.expression(e);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return next != null && prev.getText().equals(next.getText());
    }

    @Override
    public GenericEncapsulator create(PsiElement e) {
        return new GenericExpression(e);
    }

    public PsiType evaluationType() {
        return type;
    }


}
