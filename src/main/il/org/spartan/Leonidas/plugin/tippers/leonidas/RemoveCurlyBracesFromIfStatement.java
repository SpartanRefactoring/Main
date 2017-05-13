package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * Remove redundant curly braces
 *
 * author Oren Afek, Sharon Kuninin, michalcohen
 * since 06/01/17
 */
@SuppressWarnings("ConstantConditions")
public class RemoveCurlyBracesFromIfStatement implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            if (booleanExpression(0)) {
                statement(1);
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            if (booleanExpression(0))
                statement(1);
        });
    }
}
