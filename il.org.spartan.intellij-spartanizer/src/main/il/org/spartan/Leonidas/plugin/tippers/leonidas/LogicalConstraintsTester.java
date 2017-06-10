package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.INCOMPLETE;

/**
 * Testing!
 *
 * @author Sharon
 */
@TipperUnderConstruction(INCOMPLETE)
public class LogicalConstraintsTester implements LeonidasTipperDefinition {
    int x;
    @Override
    public void constraints() {
        element(1).isNot(()-> booleanExpression(2));
        element(2).asBooleanExpression.mustBeLiteral();
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            for (int i = 0; i < 1 ; i++){
                if(booleanExpression(1)){
                    x++;
                    x--;
                    x++;
                }
            }
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            if(booleanExpression(1)){
                x++;
                x--;
                x++;
            }
            /* end */
        });
    }
}
