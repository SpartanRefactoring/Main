package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.stringLiteral;

/**
 * Change Order in Equals (Prevents NullPointerException)
 *
 * @author Oren Afek
 * @since 5/29/2017.
 */
@SuppressWarnings("ALL")
public class StringLiteralEqualsChangeOrder implements LeonidasTipperDefinition {

    String identifier0;

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> /* start */ identifier0.equals(stringLiteral(1)) /* end */);
    }

    @Override
    public void replacer() {
        new Template(() -> /* start */ stringLiteral(1).equals(identifier0) /* end */);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("String s = \"Hello!\";\nSystem.out.println(s.equals(\"Bye\"));", "String s = \"Hello!\";\n System.out.println(\"Bye\".equals(s));")
                .put("\"Hello\".equals(\"World\")", null)
                .put("\"Hello\".equals(x)", null)
                .put("if(s.equals(\"Bye\")){\nSystem.out.println();\n}", "if(\"Bye\".equals(s)){\nSystem.out.println();\n}")
                .put("boolean b = s.equals(\"Bye\");", "boolean b = \"Bye\".equals(s);")
                .map();
    }
}
