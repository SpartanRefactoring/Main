package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * RemoveCurlyBracesFromIfElseStatement
 *
 * @author Oren Afek
 * @since 14/06/17
 * /
 */
@SuppressWarnings("ConstantConditions")
public class RemoveCurlyBracesFromIfElseStatement implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            if (booleanExpression(0)) {
                statement(1);
            } else {
                statement(2);
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            if (booleanExpression(0))
                statement(1);
            else
                statement(2);
            /** end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        examples.put("int x=5;\nObject a,b;\nif(a.hashCode()!=x){\n\tx = b.hashCode();\n} else {\nx=b.hashCode()+4;}",
                "int x=5;\nObject a,b;\nif(a.hashCode()!=x)\n\tx = b.hashCode();\nelse\nx=b.hashCode()+4;");
        return examples;
    }
}
