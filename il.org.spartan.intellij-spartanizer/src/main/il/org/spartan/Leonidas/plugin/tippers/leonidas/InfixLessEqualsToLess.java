package il.org.spartan.Leonidas.plugin.tippers.leonidas;


import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

/**
 * X1 <= X2 -1  ->  X1 < X2
 *
 * @author Roey Maor
 */
public class InfixLessEqualsToLess implements LeonidasTipperDefinition {
    int identifier0;
    int identifier1;

    @Override
    public void matcher() {
        new Template(() -> /* start */ identifier0 <= identifier1 - 1 /* end */);
    }

    @Override
    public void replacer() {
        new Template(() -> /* start */ identifier0 < identifier1 /* end */);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("X <= Y-1", "X < Y")
                .put("X < Y-1", null)
                .put("X <= Y", null)
                .put("if (X <= Y-1){\n\treturn true;\n}", "if (X < Y){\n\treturn true;\n}")
                .map();
    }
}
