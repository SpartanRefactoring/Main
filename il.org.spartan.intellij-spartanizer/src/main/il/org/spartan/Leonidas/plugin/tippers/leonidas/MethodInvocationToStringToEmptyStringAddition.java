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

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() ->
                identifier0 + ""
        );
    }

    @Override
    public void replacer() {
        new Template(() ->
                identifier0 + ""
        );
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("donald.toString()", "\"\" + donald")
                .map();
    }
}