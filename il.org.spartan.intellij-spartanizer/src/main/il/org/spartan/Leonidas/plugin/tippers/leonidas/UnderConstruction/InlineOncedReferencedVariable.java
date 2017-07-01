package il.org.spartan.Leonidas.plugin.tippers.leonidas.UnderConstruction;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * Inline variable
 *
 * @author Oren Afek, Michal Cohen
 * @since 20/06/17
 */
@TipperUnderConstruction(UnderConstructionReason.INCOMPLETE)
public class InlineOncedReferencedVariable implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        element(4).asStatement.refersOnce(1);
        element(3).asStatement.mustNotRefer(1);
        element(5).asStatement.mustNotRefer(1);
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            Class0 identifier1 = expression(2);
            anyNumberOf(statement(3));
            statement(4);
            all(statement(5));

            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            anyNumberOf(statement(3));
            statement(4);
            all(statement(5));
            /* end */
        });
    }

    @Override
    public void replacingRules() {
        element(4).asStatement.replaceIdentifiers(1, 2);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int x = 3;\nf(9,x);", "f(9,3);")
                .put("int x = 3;\nx++;\nf(9,x);", null)
//                .put("int y = 2;\nfor (int i = a; ; ) ;\na = y * 2;", "for (int i = a; ; ) ;\na = 2 * 2;")
                .put("int y = 2;\nfor (int i = a; ; ) ;\ny = y * 2;", null)
//                .put("int y = 2;\nfor (int i = a; ; ) ;\ny = a * 2;", null)
                .map();
    }

    class Class0 {
    }
}