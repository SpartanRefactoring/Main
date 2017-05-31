package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_REPLACER;

/**
 * Switch to Ternary (? :)
 *
 * @author Oren Afek
 * @since 30-05-2017
 */
@TipperUnderConstruction(BROKEN_REPLACER)
public class IfAssignToFooElseAssignToFoo implements LeonidasTipperDefinition {

    Object identifier0;

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            if (booleanExpression(1))
                identifier0 = expression(2);
            else
                identifier0 = expression(3);
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            identifier0 = booleanExpression(1) ? expression(2) : expression(3);
            /** end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("if (x > 0) sign = 1; else sign = -1;", "sign = x > 0 ? 1 : -1")
                .map();
    }
}