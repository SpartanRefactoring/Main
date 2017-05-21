package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Statement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.the;

/**
 * @author michalcohen
 * @since 29-04-2017.
 */
public class DemoTipper implements LeonidasTipperDefinition {
    Statement first, second;
    GenericExpression cond, ;

    @Override
    public void constraints() {
        first.isNot(() -> {
            if(booleanExpression(3)){
                statement(4);
            }
        }).ofType(PsiIfStatement.class);
        second.is(() -> {return null;});
        third.isNot(() -> !booleanExpression(5));
        first.isNot(() -> return null;});
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void matcher() {
        new Template(() -> {
            if (cond) {
                statement(first);
                statement(second);
            }
        });
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void replacer() {
        new Template(() -> {
            if (booleanExpression(0))
                statement(2);
                statement(1);
        });
    }
}