package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Remove unnecessary variable
 *
 * @author Oren Afek
 * @since 20/06/17
 */
@SuppressWarnings("ALL")
public class LocalInitializedReturn implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            Class0 identifier1 = expression(2);
            return identifier1;
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            return expression(2);
            /* end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int a = 9;\nreturn a;", "return 9;")
                .put("int a = 9;\na++;\nreturn a;", null)
                .put("a = 9;\nreturn a;", null)
                .put("int a = x + 9;\nreturn a;", "return x + 9;")
                .put("int a = 9;\nx++;\nreturn a;", null)
                .map();
    }

    class Class0 {/**/
    }
}