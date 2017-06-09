package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_MATCHER;

/**
 * Remove Unnecessary Assignment.
 *
 * @author Oren Afek
 * @since 02/06/2017
 */
@TipperUnderConstruction(BROKEN_MATCHER)
public class AssignmentAndAssignmentToSameKill implements LeonidasTipperDefinition {

    Object identifier0;

    Class3 identifier4() {
        return null;
    }

    @Override
    public void constraints() {
        //the(expression(1)).isNot(() -> identifier4());
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            identifier0 = expression(1);
            identifier0 = expression(2);
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() ->
                /** start */
                identifier0 = expression(2)
                /** end */
        );
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("x = 1;\nx = 2;", "x = 2;")
                .put("x.y = 1;\nx.y = 2;", "x.y = 2")
                .map();
    }

    class Class3 {
    }
}