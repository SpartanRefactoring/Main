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
public class StringLiteralEqualsChangeOrder implements LeonidasTipperDefinition {

    String identifier0;

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> identifier0.equals(stringLiteral(1)));
    }

    @Override
    public void replacer() {
        new Template(() -> stringLiteral(1).equals(identifier0));
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("String s = \"Hello!\";\nSystem.out.println(s.equals(\"Bye\"));", "String s = \"Hello!\";\n System.out.println(\"Bye\".equals(s));")
                .map();
    }
}
