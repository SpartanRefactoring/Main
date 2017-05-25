package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.leonidas.LeonidasUtils;

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
            if (booleanExpression(0)) {
                statement(1);
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            if (booleanExpression(0))
                statement(1);
        });
    }

    @Override
    public Map<String,String> getExamples(){
        Map<String,String> examples = new HashMap<>();
        examples.put("int x=5; Object a,b; if(a.hashCode()!=x){x = b.hashCode();}","int x=5; Object a,b; if(a.hashCode()!=x) x = b.hashCode();");
        return examples;
    }
}
