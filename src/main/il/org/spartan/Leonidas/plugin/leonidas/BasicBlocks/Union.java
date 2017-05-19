package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.step;

import java.util.List;

/**
 * @author Sharon LK
 */
public class Union extends GenericMethodCallBasedBlock {
    public static final String TEMPLATE = "union";

    public Union(PsiElement e) {
        super(e, TEMPLATE);
    }

    public Union(Encapsulator n) {
        super(n, TEMPLATE);

        List<PsiExpression> arguments = step.arguments(az.methodCallExpression(n.getInner()));
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Union() {
        super(TEMPLATE);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new Union(e);
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return false;
    }
}
