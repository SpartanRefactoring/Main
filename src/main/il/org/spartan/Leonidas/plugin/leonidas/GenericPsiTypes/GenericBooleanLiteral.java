package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Sharon LK
 */
public class GenericBooleanLiteral extends GenericPsi {
    public GenericBooleanLiteral(PsiElement inner) {
        super(inner, "Generic Boolean Literal");
    }

    @Override
    public boolean generalizes(PsiElement e) {
        return iz.booleanLiteral(e);
    }
}
