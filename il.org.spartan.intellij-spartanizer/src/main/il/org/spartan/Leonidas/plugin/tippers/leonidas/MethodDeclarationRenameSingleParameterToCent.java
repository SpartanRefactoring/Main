package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.INCOMPLETE;

/**
 * change parameter to ¢
 *
 * @author melanyc
 * @since 10-06-2017
 */
//@TipperUnderConstruction(INCOMPLETE)
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
        element(4).asStatement.mustNotRefer("cent");
        element(3).asIdentifier.notContains("cent");
    }

    // The enter under /* start */ is crucial.
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

                Class0 method1(Class2 identifier3) {
                    anyNumberOf(statement(4));
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
        Map<String, String> examples = new HashMap<>();
        // <enter examples>
        return examples;
    }

    class Class0 {
    }

    class Class2 {
    }
}