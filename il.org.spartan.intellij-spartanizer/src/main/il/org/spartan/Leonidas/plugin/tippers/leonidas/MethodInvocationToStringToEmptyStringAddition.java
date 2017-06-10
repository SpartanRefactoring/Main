package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

/**
 * s.toString() => "" + x
 *
 * @author Oren Afek
 * @since 31-05-2017
 */
public class MethodInvocationToStringToEmptyStringAddition implements LeonidasTipperDefinition {

    Object identifier0;

    void identifier1(String s) {
    }

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() ->
                /* start */
                identifier1(identifier0.toString())
                /* end */
        );
    }

    @Override
    public void replacer() {
        new Template(() ->
                /* start */
                identifier1("" + identifier0)
                /* end */
        );
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("donald.toString()", "\"\" + donald")
                .map();
    }
}