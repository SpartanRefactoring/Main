package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.INCOMPLETE;

/**
 * <Tipper description>
 * UnusedParameterToUnderscore
 *
 * @author Anna Belozovsky
 * @since 15/06/2017
 */
@TipperUnderConstruction(INCOMPLETE)
public class UnusedParameterToUnderscore implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {

        });
    }

    @Override
    public void replacer() {
        new Template(() -> {

        });
    }

    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();

        return examples;
    }

    class Class0 {

    }

}