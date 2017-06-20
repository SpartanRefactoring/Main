package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;

/**
 * No two not operations are necessary.
 *
 * @author melanyc
 * @since 30-04-2017
 */
@SuppressWarnings("DoubleNegation")
public class IfDoubleNot implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> /* start */ !(!(booleanExpression(0))) /* end */);
    }

    @Override
    public void replacer() {
        new Template(() -> /* start */ booleanExpression(0) /* end */);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("if(!(!(5==5))){\n\tSystem.out.Println(\"ok\");\n}", "if(5==5){\n\tSystem.out.Println(\"ok\");\n}")
                .put("boolean b = !(!(true));", "boolean b = true;")
                .put("for(int i = 0; !(!(i< 20)) ; ++i){\n\ti+=1;\n}", "for(int i = 0; i< 20 ; ++i){\n\ti+=1;\n}")
                .map();

    }
}