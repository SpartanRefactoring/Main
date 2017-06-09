package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * RemoveCurlyBracesFromIfStatement
 *
 * author Oren Afek, Sharon Kuninin, michalcohen
 * since 06/01/17
 */
@SuppressWarnings("ConstantConditions")
public class RemoveCurlyBracesFromIfStatement implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            if (booleanExpression(0)) {
                statement(1);
            }
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            if (booleanExpression(0))
                statement(1);
            /** end */
        });
    }

    @Override
    public Map<String,String> getExamples(){
        Map<String,String> examples = new HashMap<>();
        examples.put("int x=5;\nObject a,b;\nif(a.hashCode()!=x){\n\tx = b.hashCode();\n}","int x=5;\nObject a,b;\nif(a.hashCode()!=x)\n\tx = b.hashCode();");
        return examples;
    }
}
