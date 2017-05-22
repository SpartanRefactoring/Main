package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * @author Sharon
 */
public class LogicalConstraintsTester implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
        element(1).asBooleanExpression.mustBeLiteral();
    }

    @Override
    public void matcher() {
        new Template(() -> {
            if (booleanExpression(1)) {
                statement(2);
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            if (booleanExpression(1))
                statement(2);
        });
    }
}
