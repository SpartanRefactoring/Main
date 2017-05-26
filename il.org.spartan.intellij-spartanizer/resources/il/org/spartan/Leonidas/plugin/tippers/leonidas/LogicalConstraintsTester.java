package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiForStatement;
import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * Testing!
 *
 * @author Sharon
 */
public class LogicalConstraintsTester implements LeonidasTipperDefinition {
    int x;
    @Override
    public void constraints() {
        element(1).isNot(()-> booleanExpression(2));
        element(2).asBooleanExpression.mustBeLiteral();
    }

    @Override
    @Leonidas(PsiForStatement.class)
    public void matcher() {
        new Template(() -> {
            for (int i = 0; i < 1 ; i++){
                if(booleanExpression(1)){
                    x++;
                    x--;
                    x++;
                }
            }
        });
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void replacer() {
        new Template(() -> {
            if(booleanExpression(1)){
                x++;
                x--;
                x++;
            }
        });
    }
}
