package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.UserControlledTipper;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * change parameter to ¢
 *
 * @author melanyc
 * @since 10-06-2017
 */
@UserControlledTipper
public class MethodDeclarationRenameSingleParameterToCent implements LeonidasTipperDefinition {

    Class2 identifier3;

    @Override
    public void constraints() {
        element(1).asMethod.notContains("main");
        element(4).asStatement.mustNotRefer("cent");
        element(3).asIdentifier.notContains("cent");
        element(3).asIdentifier.notContains("__");
    }

    // The enter under /* start */ is crucial.
    @Override
    public void matcher() {
        new Template(() -> {
            class wrapping {
                /* start */

                Class0 method1$method_name(Class2 identifier3$single_parameter) {
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

                Class0 method1$method_name(Class2 identifier3$single_parameter) {
                    anyNumberOf(statement(4, "method body"));
                    return null; // ignore
                }
                /* end */
            }
        });
    }

    @Override
    public void replacingRules() {
        element(4).asStatement.replaceIdentifiers(3, "cent");
        element(3).asIdentifier.changeName("cent");
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                //.put("void foo(int x){}", "void foo(int cent){}")
                .put("void foo(int x){\n\tSystem.out.println(x);\n}", "void foo(int cent){\n\tSystem.out.println(cent);\n}")
                .put("private static void foo(int x){\n\tSystem.out.println(x);\n}", "private static void foo(int cent){\n\tSystem.out.println(cent);\n}")
                .put("private static void foo(int cent){\n\tSystem.out.println(\"\");\n}", null)
                .put("private static void foo(int x){\n\tint cent = 0;\n\tSystem.out.println(cent);\n}", null)
                .put("private static void foo(int x,int y){\n\tSystem.out.println(x);\n}", null)
                .map();

    }

    class Class0 {
    }

    class Class2 {
    }
}