package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_REPLACER;

/**
 * @author Oren Afek
 * @since 29/5/2017.
 */
@TipperUnderConstruction(BROKEN_REPLACER)
public class EliminateTryBodyEmptyNoCatchesNoFinally implements LeonidasTipperDefinition {

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            try {
                anyNumberOf(statement(0));
            } finally {
            }
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() ->
                /** start */
                anyNumberOf(statement(0))
                /** end */
        );
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("try { \n\tSystem.out.println(69);\n} finally {}", "System.out.println(69);")
                .map();
    }
}
