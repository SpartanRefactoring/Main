package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.expression;

/**
 * Decleration and return
 *
 * @author Michal Mauda
 * @since 30-09-2018
 */
@SuppressWarnings("ALL")
public class DeclarationAndReturn implements LeonidasTipperDefinition {

    class Class2{}

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            Class2 identifier0 = expression(1);
            return identifier0;
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            return expression(1);
            /* end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("looney = \"Toons\";\nreturn looney;", null)
                .put("String looney = \"Toons\";\nint x = 2;\nreturn looney;", null)
                .put("String looney = \"Toons\";\nreturn looney;", "return \"Toons\";")
                .put("int x = 2;\nreturn x;", "return 2;")
                .map();
    }
}
