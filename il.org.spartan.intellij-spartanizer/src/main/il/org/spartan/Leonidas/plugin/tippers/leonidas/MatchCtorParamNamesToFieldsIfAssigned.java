package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.INCOMPLETE;

/**
 * <Tipper description>
 * MatchCtorParamNamesToFieldsIfAssigned
 *
 * @author Anna Belozovsky
 * @since 15/06/2017
 */
@TipperUnderConstruction(INCOMPLETE)
public class MatchCtorParamNamesToFieldsIfAssigned implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
//        element(4).asStatement.
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            class Class0 {
                Class1 field2;

                Class0(Class1 identifier3) {
                    anyNumberOf(statement(4));
                }
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
           /* start */
            class Class0 {
                Class1 field2;

                Class0(Class1 identifier3) {
                    anyNumberOf(statement(4));
                }
            }
            /* end */
        });
    }

    @Override
    public void replacingRules() {
        String newIdentifier = element(2).asIdentifier.getText();
        element(4).asStatement.replaceIdentifiers(3, newIdentifier);
        element(3).asIdentifier.changeName(newIdentifier);
    }

    class Class1 {

    }

}