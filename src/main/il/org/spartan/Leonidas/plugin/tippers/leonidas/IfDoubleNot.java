package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.BooleanExpression;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;

/**
 * No two not operations are necessary.
 *
 * @author melanyc
 * @since 30-04-2017
 */
@SuppressWarnings("DoubleNegation")
public class IfDoubleNot implements LeonidasTipperDefinition {

    BooleanExpression boolExp;
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
        new Template(() -> !(!(booleanExpression(0))));
    }

    @Override
    public void replacer() {
        new Template(() -> booleanExpression(0));
    }
}