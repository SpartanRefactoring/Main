package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

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
     */
    @Override
    public void constraints() {
        element(1).asStatement.isNotDeclarationStatement();
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
        return new ExampleMapFactory()
                .put("int x=5;\nObject a,b;\nwhile(a.hashCode()!=x){\n\tx = b.hashCode();\n}", "int x=5;\nObject a,b;\nwhile(a.hashCode()!=x)\n\tx = b.hashCode();")
//                .put("while(true)\n\twhile(true){\nSystem.out.println();\n}", "while(true)\n\twhile(true)\nSystem.out.println();\n")
                .put("while(true){\n\twhile(true){\nSystem.out.println();\n}}", "while(true){\n\twhile(true)\nSystem.out.println();\n}")
                .put("while(true)\n\tSystem.out.println();", null)
                .put("while(true)\n\twhile(true)\n\t\tSystem.out.println();", null)
                .map();
    }
}