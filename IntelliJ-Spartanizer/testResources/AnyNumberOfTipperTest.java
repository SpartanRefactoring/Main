package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiWhileStatement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * AnyNumberOfTipperTest
 *
 * @author michalcohen
 * @since 30-04-2017
 */
@SuppressWarnings("ConstantConditions")
public class AnyNumberOfTipperTest implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The constraint are of the form:
     * the(<generic element>(<id>)).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     */
    @Override
    public void constraints() {
    }

    @Override
    @Leonidas(PsiWhileStatement.class)
    public void matcher() {
        new Template(() -> {
            int x;
            while (booleanExpression(0)){
                x++;
                anyNumberOf(statement(1));
                x--;
            }
        });
    }

    @Override
    @Leonidas(PsiWhileStatement.class)
    public void replacer() {
        new Template(() -> {
            while (booleanExpression(0)) {
                anyNumberOf(statement(1));
            }
        });
    }
}