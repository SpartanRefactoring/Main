package spartanizer.LeonidasTippers;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

/**
 * Remove redundent curly braces
 * @author Oren Afek
 * @since 06/01/17
 */
public class RemoveCurlyBracesFromIfStatement extends GenericPsiElementStub {

    @Leonidas(PsiIfStatement.class)
    public void from(){

    }

    @Leonidas(PsiIfStatement.class)
    public void to(){
        if (booleanExpression(0))
            statement(1);
    }

    public moreConstrains() {
        new Constraint(1) {
            @Override
            public void tempolate() {
                !booleanExpression(2);
            }
        }

        new Constraint(2) {
            @Override
            public void tempolate() {
                !booleanExpression(3);
            }
        }
    }
}
