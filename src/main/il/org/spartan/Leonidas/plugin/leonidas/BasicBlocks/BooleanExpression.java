package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

/**
 * @author michalcohen
 * @since 08-05-2017.
 */
public class BooleanExpression extends Expression {
    private static final String TEMPLATE = "booleanExpression";
    public Boolean value;
    PsiType type;

    public BooleanExpression(PsiElement e, PsiType type) {
        super(e, PsiType.BOOLEAN);
        this.type = type;
    }

}
