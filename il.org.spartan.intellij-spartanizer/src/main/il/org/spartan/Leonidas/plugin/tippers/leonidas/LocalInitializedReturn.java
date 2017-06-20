package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Remove unnecessary variable
 *
 * @author Oren Afek
 * @since 20/06/17
 */
public class LocalInitializedReturn implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The structural constraint are of the form:
     * element(<id>).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     * There are non structural that you can apply, for example
     * element(<id>).asMethod.startsWith("set);
     */
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            Class0 identifier1 = expression(2);
            return identifier1;
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            return expression(2);
            /* end */
        });
    }

    /**
     * Defines code examples and results after applying the tipper.
     * This is used to test the tipper.
     * example:
     * examples.put("!(!(x > 4))", "x > 4");
     */
    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("int a = 9;\nreturn a;", "return 9;")
                .map();
    }

    class Class0 {/**/
    }
}