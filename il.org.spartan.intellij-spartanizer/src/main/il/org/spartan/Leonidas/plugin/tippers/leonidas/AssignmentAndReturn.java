package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Assignment and return
 *
 * @author Oren Afek
 * @since 31-5-2017
 */
public class AssignmentAndReturn implements LeonidasTipperDefinition {

    Object identifier0;

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            identifier0 = expression(1);
            return identifier0;
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            return identifier0 = expression(1);
            /* end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("looney = \"Toons\";\nreturn looney;", "return looney = \"Toons\";")
                .put("looney = \"Toons\";\nint x = 2;\nreturn looney;", null)
                .put("String looney = \"Toons\";\nreturn looney;", null)
                .put("x = 2;\nreturn x;", "return x=2;")
                .map();
    }
}