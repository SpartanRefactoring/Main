package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Trenary Eliminate
 *
 * @author Amir Sagiv
 * @since 28-06-2017
 */
public class TernaryEliminate implements LeonidasTipperDefinition {


    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() ->/* start */ booleanExpression(0) ? expression(1) : expression(1)/* end */);
    }

    @Override
    public void replacer() {
        new Template(() -> /* start */ expression(1) /* end */);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("x == 9 ? 9:9", "9")
                .put("true ? false : false", "false")
                .put("if(( x == a ? b : b) == b){}", "if ((b) == b) {}")
                .put("true? true:false", null)
                .put("x? x:y", null)
                .put("return x? x:x;", "return x;")
                .map();
    }

}