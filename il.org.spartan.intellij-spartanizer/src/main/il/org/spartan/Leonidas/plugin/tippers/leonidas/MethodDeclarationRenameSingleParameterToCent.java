package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

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
public class MethodDeclarationRenameSingleParameterToCent implements LeonidasTipperDefinition {

    Class2 identifier3;

    /**
     * Write here additional constraints on the matcher tree.
     * The structural constraint are of the form:
     * element(<id>).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     * There are non structural that you can apply, for example
     * element(<id>).asMethod.startsWith("set);
     */
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

                Class0 method1$main(Class2 identifier3$TheFromParamName) {
                    anyNumberOf(statement(4, "BodyOfMethod"));
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

                Class0 method1(Class2 identifier3$TheToParamName) {
                    anyNumberOf(statement(4, "BodyOfMethod"));
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

    /**
     * Defines code examples and results after applying the tipper.
     * This is used to test the tipper.
     * example:
     * examples.put("!(!(x > 4))", "x > 4");
     */
    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("void foo(int x){}", "void foo(int cent){}")
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