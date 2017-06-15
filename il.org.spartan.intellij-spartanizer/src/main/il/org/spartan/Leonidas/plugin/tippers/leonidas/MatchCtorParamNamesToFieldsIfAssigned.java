package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

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
    }

    @Override
    /* If clarification of the type of the tipper
     * is needed, use the annotation @Leonidas(<Psi Class>)
     * when psi class is the class of the element on which the tipper applied
     */
    public void matcher() {
        new Template(() -> {
            /* start */


            /* end */
        });
    }

    @Override
    /* If clarification of the type of the tipper
     * is needed, use the annotation @Leonidas(<Psi Class>)
     * when psi class is the class of the element on which the tipper applied
     */
    public void replacer() {
        new Template(() -> {
            /* start */


            /* end */
        });
    }
}