package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * FluentSetter
 *
 * @author michalcohen, amirsagiv83
 * @since 29-05-17
 */
@SuppressWarnings("ALL")
public class FluentSetter implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        element(1).asMethod.startsWith("set");
        element(4).isNot(() -> {
            return;
        });
    }

    @Override
    public void matcher() {
        new Template(() -> {
            class Class0 {
                /* start */

                void method1(Class2 identifier3) {
                    anyNumberOf(statement(4));
                }
                /* end */
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {

            class Class0 {
                /* start */

                Class0 method1(Class2 identifier3) {
                    anyNumberOf(statement(4));
                    return this;
                }
                /* end */
            }
        });
    }


    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("public class A{\n\tint x;\n\n\tvoid setX(int x){\n\t\tthis.x = x;\n\t}\n}",
                        "public class A{\n\tint x;\n\n\tA setX(int x){\n\t\tthis.x = x;\n\t\treturn this;\n\t}\n}")
                .put("public class A{\n\tint x;\n\tdouble y;\n\n\tdouble getY(){\n\t\treturn y;\n\t}\n\n\tvoid setX(int x){\n\t\tthis.x = x;\n\t}\n}",
                        "public class A{\n\tint x;\n\tdouble y;\n\n\tdouble getY(){\n\t\treturn y;\n\t}\n\n\tA setX(int x){\n\t\tthis.x = x;\n\t\treturn this;\n\t}\n}")
                .put("public class A{\n\tint x;\n\n\tvoid foo(int x){\n\t\tthis.x = x;\n\t}\n}",
                        null)
                .put("public class A{\n\tvoid setX(){\n\t\t;\n\t}\n}", null)
                .map();

    }

    @Override
    public void replacingRules() {
        element(0).asType.setAsOuterClass();
    }

    class Class2 {
    }
}