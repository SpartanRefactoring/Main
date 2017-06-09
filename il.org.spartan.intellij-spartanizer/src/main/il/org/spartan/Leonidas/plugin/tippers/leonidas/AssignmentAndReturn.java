package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Assignment and return
 *
 * @author Oren Afek
 * @since 31/5/2017
 */

public class AssignmentAndReturn implements LeonidasTipperDefinition {

    Object identifier0;
    Object identifier1;

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            identifier0 = expression(1);
            return identifier1;
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            return identifier1 = expression(1);
            /** end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("String looney = \"Toons\";\nreturn lonney;", "return looney = \"Toons\";")
                .map();
    }
}