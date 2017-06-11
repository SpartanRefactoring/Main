package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * if while statement contains only one statement, its curly braces can be removed
 *
 * @author michalcohen
 * @since 30-04-2017
 */
@SuppressWarnings("ConstantConditions")
public class RemoveCurlyBracesFromWhileStatement implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            while (booleanExpression(0)) {
                statement(1);
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            while (booleanExpression(0))
                statement(1);
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        examples.put("int x=5;\nObject a,b;\nwhile(a.hashCode()!=x){\n\tx = b.hashCode();\n}","int x=5;\nObject a,b;\nwhile(a.hashCode()!=x)\n\tx = b.hashCode();");
        return examples;
    }
}