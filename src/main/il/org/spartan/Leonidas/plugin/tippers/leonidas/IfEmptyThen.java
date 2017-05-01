package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.statement;

/**
 * Replace if(b); else{s;} with if(!b){s;}
 * IfEmptyThen
 *
 * @author melanyc
 * @since 30-04-2017
 */
public class IfEmptyThen implements LeonidasTipperDefinition {

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
            if (booleanExpression(0))
                ;
            else
                statement(1);
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            if (!(booleanExpression(0)))
                statement(1);
        });
    }
}