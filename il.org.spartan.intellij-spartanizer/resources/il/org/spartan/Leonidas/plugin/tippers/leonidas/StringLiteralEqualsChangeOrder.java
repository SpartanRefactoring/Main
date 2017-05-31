package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.stringLiteral;

/**
 * Change Order in Equals (Prevents NullPointerException)
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
        new Template(() -> {
            /** start */
            identifier0.equals(stringLiteral(1));
            /** end */

        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            stringLiteral(1).equals(identifier0);
            /** end */

        });
    }

    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        examples.put("String s = \"Hello!\"; System.out.println(s.equals(\"Bye\"));", "String s = \"Hello!\"; System.out.println(\"Bye\".equals(s));");
        return examples;
    }
}
