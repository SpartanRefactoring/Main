package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

/**
 * X1 -1 >= X2  ->  X1 > X2
 *
 * @author Roey Maor
 */
@SuppressWarnings("ALL")
public class InfixGreaterEqualToGreater implements LeonidasTipperDefinition {
    int identifier0;
    int identifier1;

    @Override
    public void matcher() {
        new Template(() -> /* start */ identifier0 - 1 >= identifier1 /* end */);
    }

    @Override
    public void replacer() {
        new Template(() -> /* start */ identifier0 > identifier1 /* end */);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("X-1 >= Y", "X > Y")
                .put("X-1 > Y", null)
                .put("X >= Y", null)
                .put("if (y - 1 >= z){\n\treturn true;\n}", "if (y > z){\n\treturn true;\n}")
                .map();
    }
}
