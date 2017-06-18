package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.UNTESTED;

/**
 * change unused parameter to __
 *
 * @author Anna Belozovsky
 * @since 15/06/2017
 */
@TipperUnderConstruction(UNTESTED)
public class UnusedParameterToUnderscore implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
        element(4).asStatement.mustNotRefer(3);
        element(3).asIdentifier.notContains("__");
    }

    @Override
    public void matcher() {
        new Template(() -> {
            class wrapping {
                /* start */

                Class0 method1(Class2 identifier3) {
                    anyNumberOf(statement(4));
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

                Class0 method1(Class2 __) {
                    anyNumberOf(statement(4));
                    return null; // ignore
                }
                /* end */
            }
        });
    }

    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        examples.put("int foo(int x){\n\treturn 2;\n}", "int foo(int __){\n\treturn 2;\n}");
        return examples;
    }

    private class Class0 {

    }

    private class Class2 {

    }

}