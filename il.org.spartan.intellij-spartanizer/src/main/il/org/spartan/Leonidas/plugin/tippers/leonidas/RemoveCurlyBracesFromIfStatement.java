package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * RemoveCurlyBracesFromIfStatement
 * <p>
 * author Oren Afek, Sharon Kuninin, michalcohen
 * since 06/01/17
 */
@SuppressWarnings("ConstantConditions")
public class RemoveCurlyBracesFromIfStatement implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        element(1).asStatement.isNotDeclarationStatement();
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            if (booleanExpression(0)) {
                statement(1);
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
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int x=5;\nObject a,b;\na = new Object();\nb = new Object();\nif(a.hashCode()!=x){\n\tx = b.hashCode();\n}", "int x=5;\nObject a,b;\n a = new Object();\nb = new Object();\nif(a.hashCode()!=x)\n\tx = b.hashCode();")
//                .put("if(true)\n\tif(true){\nSystem.out.println();\n}", "if(true)\n\tif(true)\nSystem.out.println();\n")
                .put("if(true){\n\tif(true){\nSystem.out.println();\n}}", "if(true){\n\tif(true)\nSystem.out.println();\n}")
                .put("if(true)\n\tSystem.out.println();", null)
                .put("if(true)\n\tif(true)\n\t\tSystem.out.println();", null)
                .put("if(true){\n\tif(true){\nSystem.out.println();\n}}else{\nSystem.out.println();\n}", "if(true){\n\tif(true)\nSystem.out.println();\n}else{\nSystem.out.println();\n}")
                .map();

    }
}
