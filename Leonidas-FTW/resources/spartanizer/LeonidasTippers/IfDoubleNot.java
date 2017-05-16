package spartanizer.LeonidasTippers;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

/**
 * Replace if(!(!(b))); with if(b)
 *
 * @author Oren Afek
 * @since 01-12-17
 */
public class IfDoubleNot extends GenericPsiElementStub {

    @Leonidas(PsiIfStatement.class)
    public void from() {
        if (booleanExpression(0))
			anyBlock(1);

    }

    @Leonidas(PsiIfStatement.class)
    public void to() {
        if (booleanExpression(0))
			anyBlock(1);

    }
}
