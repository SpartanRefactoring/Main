package spartanizer.LeonidasTippers;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

/**
 * Replace if(e1 > e2); with if(e2 < e1)
 *
 * @author michalcohen
 * @since 21-01-17
 */
public class GreaterConditionToSmaller extends GenericPsiElementStub {

    @Leonidas(PsiIfStatement.class)
    public void from() {
        if (booleanExpression(0) > booleanExpression(1))
			anyBlock(2);

    }

    @Leonidas(PsiIfStatement.class)
    public void to(){
        if (booleanExpression(1) < booleanExpression(0))
			anyBlock(2);

    }
}
