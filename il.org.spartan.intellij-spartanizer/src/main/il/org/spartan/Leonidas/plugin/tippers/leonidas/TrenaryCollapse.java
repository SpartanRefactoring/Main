package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Trenary Collapse
 *
 * @author Amir Sagiv
 * @since 28-06-2017
 */
public class TrenaryCollapse implements LeonidasTipperDefinition {


    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() ->/* start */ booleanExpression(0) ? booleanExpression(1) ? expression(2) : expression(3) : expression(3) /* end */);
    }

    @Override
    public void replacer() {
        new Template(() -> /* start */ (booleanExpression(0) && booleanExpression(1)) ? expression(2) : expression(3) /* end */);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("x == 9 ? y == 8? 5:9:9", "(x==9 && y == 8) ? 5 : 9")
                .put("true ? false? true: false : false", "(true && false) ? true : false")
//                .put("if((x == x ? y == y? a:b:b) == b){}", "if (((x == x && y == y) ? a : b) == b) {}")
                .put("true? false? 5:9:7", null)
                .put("return x ? y ? z : x : x;", "return (x && y) ? z : x;")
                .put("return x ? y ? z : a : b;", null)
                .map();
    }

}