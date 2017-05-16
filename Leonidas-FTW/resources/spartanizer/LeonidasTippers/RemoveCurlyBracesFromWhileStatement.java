package spartanizer.LeonidasTippers;

import com.intellij.psi.PsiWhileStatement;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

/**
 * Remove redundent curly braces
 *
 * @author Oren Afek
 * @since 01-12-17
 */
public class RemoveCurlyBracesFromWhileStatement extends GenericPsiElementStub {

    @Leonidas(PsiWhileStatement.class)
    public void from() {
        while (booleanExpression(0))
			statement(1);
    }

    @Leonidas(PsiWhileStatement.class)
    public void to() {
        while (booleanExpression(0))
            statement(1);
    }
}
