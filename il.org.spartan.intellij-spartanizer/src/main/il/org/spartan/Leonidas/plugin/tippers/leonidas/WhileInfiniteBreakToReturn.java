package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;


/**
 * break and return after infinite loop => return inside infinite loop
 *
 * @author Roey Maor
 * @since 29/06/2017
 */
@SuppressWarnings("ALL")
public class WhileInfiniteBreakToReturn implements LeonidasTipperDefinition {

    Object identifier3;

    @Override
    public void matcher() {
        new Template(() -> {
            /*start*/
            while (true) {
                anyNumberOf(statement(0));
                if (booleanExpression(1)) {
                    break;
                }
            }
            return expression(2);
            /*end*/

        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /*start*/
            while (true) {
                anyNumberOf(statement(0));
                if (booleanExpression(1)) {
                    return expression(2);
                }
            }
            /*end*/
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("while(true){\n" +
                                "                int x = 5;\n" +
                                "                if(x==5){\n" +
                                "                    break;\n" +
                                "                }\n" +
                                "            }\n" +
                                "            return x;",
                        "while(true){\n" +
                                "                int x = 5;\n" +
                                "                if(x==5){\n" +
                                "                    return x;\n" +
                                "                }\n" +
                                "            }")

                .put("while(true){\n" +
                                "                int x = 5;\n" +
                                "                int y = 6;\n" +
                                "                if(x==5){\n" +
                                "                    break;\n" +
                                "                }\n" +
                                "            }\n" +
                                "            return x;",
                        "while(true){\n" +
                                "                int x = 5;\n" +
                                "                int y = 6;\n" +
                                "                if(x==5){\n" +
                                "                    return x;\n" +
                                "                }\n" +
                                "            }")
                .put("while(true){\n" +
                        "                int x = 5;\n" +
                        "                if(x==5){\n" +
                        "                    break;\n" +
                        "                }\n" +
                        "            }\n", null)
                .put("while(true){\n" +
                        "                int x = 5;\n" +
                        "                if(x==5){\n" +
                        "                    continue;\n" +
                        "                }\n" +
                        "            }\n" +
                        "            return x;", null)
                .map();
    }

    class Class0 {
    }
}
