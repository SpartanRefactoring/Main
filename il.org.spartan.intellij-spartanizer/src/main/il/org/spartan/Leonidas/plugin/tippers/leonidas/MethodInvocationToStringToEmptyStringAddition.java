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
                /* start */
                identifier0.toString()
                /* end */
        );
    }

    @Override
    public void replacer() {
        new Template(() ->
                /* start */
                "" + identifier0
                /* end */
        );
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("donald.toString()", "\"\" + donald")
                .put("donald.toString();", "\"\" + donald;")
                .put("donald.string()", null)
                .put("donald.string().equals()", null)
                .put("String str2 = i.toString() + \"whwh\";", "String str2 = \"\" + i + \"whwh\";")
                .map();
    }
}