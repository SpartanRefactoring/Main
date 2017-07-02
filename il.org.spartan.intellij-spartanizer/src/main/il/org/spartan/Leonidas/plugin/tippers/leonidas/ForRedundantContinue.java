package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;

/**
 * Remove redundant continue
 *
 * @author Oren Afek
 * @since 20-06-2017
 */
public class ForRedundantContinue implements LeonidasTipperDefinition {


    public static int expression(int id) {
        return 0;
    }

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            for (statement(0); booleanExpression(1); statement(2)) {
                anyNumberOf(statement(3));
                continue;
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            for (statement(0); booleanExpression(1); statement(2)) {
                anyNumberOf(statement(3));
            }
            /* end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("for(int i = 0; i < 10; i++){\n System.out.println(i);\n continue;\n}", "for(int i = 0; i < 10; i++){\nSystem.out.println(i);\n}")
                .put("for(int i = 0; i < 10; i++){\n System.out.println(i);\n continue;\n System.out.println(i);\n}", null)
                .put("for(int i = 0; i < 10; i++){\n System.out.println(i);\n if(true)\n\tcontinue;\n}", null)
                .put("for(int i = 0; i < 10; i++){\n System.out.println(i);\n System.out.println(i);\n System.out.println(i);\ncontinue;\n}", "for(int i = 0; i < 10; i++){\nSystem.out.println(i);\n System.out.println(i);\n System.out.println(i);\n}")
                .map();
    }
}