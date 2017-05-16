package spartanizer.FutureLeonidasTippers;

import com.intellij.psi.PsiArrayAccessExpression;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

/**
 * Replace arr[index]; index++ with arr[index++]
 *
 * @author Oren Afek
 * @since 10/01/17
 */
public class InliningPrefix extends GenericPsiElementStub {

    @Leonidas(PsiArrayAccessExpression.class)
    public void from(){
        arrayIdentifier(0)[identifier(1)];
        identifier(1)++;

    }

    @Leonidas(PsiArrayAccessExpression.class)
    public void to(){
        arrayIdentifier(0)[identifier(1)++];
    }
}
