package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * RemoveCurlyBracesFromIfElseStatement
 *
 * @author Oren Afek
 * @since 14/06/17
 *
 */
@SuppressWarnings("ALL")
public class RemoveCurlyBracesFromIfElseStatement implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        element(1).isNot(() -> {
            if (booleanExpression(3)){
                anyNumberOf(statement(4));
            }
        });
        element(1).isNot(() -> {
            if (booleanExpression(5))
                statement(6);
        });
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
            /* start */
            if (booleanExpression(0))
                statement(1);
            else
                statement(2);
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int x=5;\nObject a,b;\nif(a.hashCode()!=x){\n\tx = b.hashCode();\n} else {\nx=b.hashCode()+4;}",
                        "int x=5;\nObject a,b;\nif(a.hashCode()!=x)\n\tx = b.hashCode();\nelse\nx=b.hashCode()+4;")
                .put("if(true){\n\tSystem.out.println();\n} else {\n\tSystem.out.println();\n}", "if(true)\n\tSystem.out.println();\n else\n\tSystem.out.println();\n")
                .put("if(true)\n\tSystem.out.println();\n else {\n\tSystem.out.println();\n}", null)
                .put("if(true){\n\tSystem.out.println();\n} else\n\tSystem.out.println();", null)
                .put("if(true)\n\tSystem.out.println();\n else\n\tSystem.out.println();", null)
                .put("if(true){\nif(true){\n\tSystem.out.println();\n} else{\n\tSystem.out.println();\n}\n}", "if(true){\nif(true)\n\tSystem.out.println();\n else\n\tSystem.out.println();\n}")
                .put("if(true)\nif(true){\n\tSystem.out.println();\n} else{\n\tSystem.out.println();\n}", "if(true)\nif(true)\n\tSystem.out.println();\n else\n\tSystem.out.println();")
                .map();
    }
}
