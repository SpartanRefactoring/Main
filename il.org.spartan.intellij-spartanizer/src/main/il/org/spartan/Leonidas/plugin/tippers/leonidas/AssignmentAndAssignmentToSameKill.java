package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * Remove Unnecessary Assignment.
 *
 * @author Oren Afek
 * @since 02-06-2017
 */
public class AssignmentAndAssignmentToSameKill implements LeonidasTipperDefinition {

    Object variable0;

    @Override
    public void constraints() {
        element(2).asExpression.mustNotRefer(0);
        element(1).asExpression.DoesNotContainMethodCallExpression(); // might change state.
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            variable0 = expression(1);
            variable0 = expression(2);
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() ->
        {
            /* start */
            variable0 = expression(2);
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("x = 1;\nx = 2;", "x = 2;")
                .put("x.y = 1;\nx.y = 2;", "x.y = 2;")
                .put("x = 1;\nx1 = 2;", null)
                .put("x = 1;\nx = x + 1;", null)
                .map();
    }
}