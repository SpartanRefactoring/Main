package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Change: if(x) return e; return f => return x ? e : f;
 *
 * @author Oren Afek
 * @since 14/06/17
 */
public class IfReturnNoElseReturn implements LeonidasTipperDefinition {


    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            if (booleanExpression(0))
                return expression(1);
            return expression(2);
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> booleanExpression(0) ? expression(1) : expression(2));
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("if(l.size() > 0)\nreturn l.get(0);\nreturn null;", "return l.size() > 0 ? l.get(0) : null;")
                .map();
    }
}