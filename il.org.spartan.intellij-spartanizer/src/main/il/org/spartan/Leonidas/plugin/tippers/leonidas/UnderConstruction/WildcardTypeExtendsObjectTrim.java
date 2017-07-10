package il.org.spartan.Leonidas.plugin.tippers.leonidas.UnderConstruction;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.INCOMPLETE;


/**
 * ? extends Object => ?
 *
 * @author Roey Maor
 * @since 29/06/2017
 */
@SuppressWarnings("ALL")
@TipperUnderConstruction(INCOMPLETE)
public class WildcardTypeExtendsObjectTrim implements LeonidasTipperDefinition {

    Object identifier3;

    @Override
    public void matcher() {
        new Template(() -> {

            class wrapping {
                /* start */
                Class0 method1(Class2<? extends Object> identifier3) {
                    anyNumberOf(statement(4, "method body"));
                    return null; // ignore
                }
            /* end */
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {

            class wrapping {
                /* start */
                Class0 method1(Class2<?> identifier3) {
                    anyNumberOf(statement(4, "method body"));
                    return null; // ignore
                }
            /* end */
            }
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("public void drawAll(List<? extends Object> shapes) {\n" +
                                "    for (Shape s: shapes) {\n" +
                                "        s.draw(this);\n" +
                                "   }\n" +
                                "}",
                        "public void drawAll(List<?> shapes) {\n" +
                                "    for (Shape s: shapes) {\n" +
                                "        s.draw(this);\n" +
                                "   }\n" +
                                "}")
                .put("public void drawAll(Object shapes) {\n" +
                        "    for (Shape s: shapes) {\n" +
                        "        s.draw(this);\n" +
                        "   }\n" +
                        "}", null)
                .map();
    }

    class Class0{

    }

    class Class2<T> {
    }


}
