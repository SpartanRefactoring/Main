package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * Inline variable
 *
 * @author Oren Afek, Michal Cohen
 * @since 20/06/17
 */
public class InlineOncedReferencedVariable implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        element(4).asStatement.refersOnce(1);
        element(3).asStatement.mustNotRefer(1);
    }

    class Class0{}
    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            Class0 identifier1 = expression(2);
            anyNumberOf(statement(3));
            statement(4);
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            anyNumberOf(statement(3));
            statement(4);
            /* end */
        });
    }

    @Override
    public void replacingRules() {
        element(4).asStatement.replaceIdentifiers(1,2);
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int x = 3;\nf(9,x);","f(9,3);")
                .put("int x = 3;\nx++;\nf(9,x);","int x = 3;\nx++;\nf(9,x);")
                .map();
    }
}