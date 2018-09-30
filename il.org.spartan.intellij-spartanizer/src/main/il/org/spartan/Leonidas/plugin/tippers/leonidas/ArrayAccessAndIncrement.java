package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * Inline Next Increment
 *
 * @author Oren Afek
 * @since 29/6/17
 */
@SuppressWarnings("ALL")
public class ArrayAccessAndIncrement implements LeonidasTipperDefinition {


    Object[] identifier0 = new Object[]{};
    private int identifier1;

    @Override
    public void constraints() {
        element(2).asExpression.mustNotRefer(1);
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            identifier0[identifier1] = expression(2);
            identifier1++;
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            identifier0[identifier1++] = expression(2);
            /* end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("array[i] = 69;\ni++;", "array[i++] = 69;")
                .put("if(true){\narray[i] = 69;\ni++;\n}", "if(true){\narray[i++] = 69;\n}")
                .put("array[i] = 69;\nj++;", null)
                .put("for(int i=0; i<5; array[i] = 69, i++){\n;\n};", null)
                .map();
    }
}