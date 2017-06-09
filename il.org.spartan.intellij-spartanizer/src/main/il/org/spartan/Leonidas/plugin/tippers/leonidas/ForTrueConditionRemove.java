package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_MATCHER;

/**
 * for(?;true;?) => for(?;;?)
 *
 * @author Oren Afek
 * @since 31/05/2017
 */
@TipperUnderConstruction(BROKEN_MATCHER)
public class ForTrueConditionRemove implements LeonidasTipperDefinition {

    int identifier0;
    int identifier1;

    @Override
    public void constraints() {

    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            for (int identifier0 = expression(1); true; identifier1++)
                anyNumberOf(statement(2));
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            for (int identifier0 = expression(1); ; identifier1++)
                anyNumberOf(statement(2));
            /** end */
        });
    }

    /**
     * Defines code examples and results after applying the tipper.
     * This is used to test the tipper.
     * example:
     * examples.put("!(!(x > 4))", "x > 4");
     */
    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("for(int i = 0; true; ++i){\n\tSystem.out.println(i);\n}", "for(int i = 0;; ++i){\n\tSystem.out.println(i);\n}")
                .map();
    }
}