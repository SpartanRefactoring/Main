package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.the;

/**
 * @author michalcohen
 * @since 29-04-2017.
 */
public class TestTipper implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
        the(statement(1)).isNot(() -> {
            if(booleanExpression(3)){
                statement(4);
            }
        }).ofType(PsiIfStatement.class);
        the(statement(2)).is(() -> return null;);
        the(booleanExpression(3)).isNot(() -> !booleanExpression(5));
        the(statement(1)).isNot(() -> return null;);
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void matcher() {
        new Template(() -> {
            if (booleanExpression(0)) {
                statement(1);
                statement(2);
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