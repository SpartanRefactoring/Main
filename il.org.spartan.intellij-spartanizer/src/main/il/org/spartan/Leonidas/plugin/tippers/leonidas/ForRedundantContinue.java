package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_MATCHER;

/**
 * Remove redundant continue
 *
 * @author Oren Afek
 * @since 20-06-2017
 */

@TipperUnderConstruction(BROKEN_MATCHER)
public class ForRedundantContinue implements LeonidasTipperDefinition {


    public static int expression(int id) {
        return 0;
    }

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            for (statement(0); booleanExpression(1); statement(2)) {
                anyNumberOf(statement(3));
                continue;
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            for (statement(0); booleanExpression(1); statement(2)) {
                anyNumberOf(statement(3));
            }
            /* end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("for(int i = 0; i < 10; i++){\n System.out.println(i);\ncontinue;\n}", "for(int i = 0; i < 10; i++){\nSystem.out.println(i);\n}")
                .map();
    }
}