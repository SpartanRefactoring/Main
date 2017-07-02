package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * Eliminate redundant try-finally : try{x;} finally {} => x;
 *
 * @author Oren Afek
 * @since 29/5/2017.
 */
public class EliminateTryBodyEmptyNoCatchesNoFinally implements LeonidasTipperDefinition {

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            try {
                anyNumberOf(statement(0));
            } finally {
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() ->
                /* start */
                anyNumberOf(statement(0))
                /* end */
        );
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("try { \n\tSystem.out.println(69);\n} finally {}", "System.out.println(69);")
                .put("try { \n\tSystem.out.println(69);\n} catch(Exception e){}\nfinally {}", null)
                .put("try { \n\tSystem.out.println(69);\n} finally { \n\tSystem.out.println(70);\n}", null)
                .put("try { \n\tSystem.out.println(69);\n\n\tSystem.out.println(70);\n\n\tSystem.out.println(71);\n} finally {}", "System.out.println(69);\n\n\tSystem.out.println(70);\n\n\tSystem.out.println(71);")
                .put("try { \n\tSystem.out.println(69);\n} catch(Exception e){}", null)
                .map();
    }
}
