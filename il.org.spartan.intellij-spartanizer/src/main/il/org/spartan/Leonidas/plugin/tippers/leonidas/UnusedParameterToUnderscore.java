package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.UNTESTED;

/**
 * <Tipper description>
 * UnusedParameterToUnderscore
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
        element(4).asStatement.mustNotRefer(element(3).asIdentifier.getText());
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

        return examples;
    }

    private class Class0 {

    }

    private class Class2 {

    }

}