package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.statement;

/**
 * if while statement contains only one statement, its curly braces can be removed
 * RemoveCurlyBracesFromWhileStatement
 *
 * @author michalcohen
 * @since 30-04-2017
 */
public class RemoveCurlyBracesFromWhileStatement implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            while (booleanExpression(0)) {
                statement(1);
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            while (booleanExpression(0))
                statement(1);
        });
    }
}