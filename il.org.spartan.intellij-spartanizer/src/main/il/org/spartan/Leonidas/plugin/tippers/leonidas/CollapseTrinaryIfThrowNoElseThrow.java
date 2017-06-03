package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_MATCHER;

/**
 * Collapse Ternary If Throw No Else Throw
 *
 * @author Oren Afek
 * @since 30/5/2017.
 */

//@TipperUnderConstruction(BROKEN_MATCHER)
public class CollapseTrinaryIfThrowNoElseThrow implements LeonidasTipperDefinition {

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            if (booleanExpression(0))
                expression(1);
            expression(2);
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() ->
                /** start */
                booleanExpression(0) ? expression(1) : expression(2)
                /** end */
        );
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("if (goophy != bucks) throw new LooneyToonesException(); throw SameLooneyToones();",
                        "throw goophy != bucks ? new LooneyToonesException() : SameLooneyToones();")
                .map();
    }
}
