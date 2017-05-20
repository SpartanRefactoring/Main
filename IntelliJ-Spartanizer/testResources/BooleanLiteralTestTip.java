package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiWhileStatement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;

/**
 * BooleanLiteralTestTip.java
 *
 * @author michalcohen
 * @since 30-04-2017
 */
@SuppressWarnings("ConstantConditions")
public class BooleanLiteralTestTip implements LeonidasTipperDefinition {

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
            while (booleanLiteral(0)){
                statement(1);
            }
        });
    }

    @Override
    @Leonidas(PsiWhileStatement.class)
    public void replacer() {
        new Template(() -> {
            while (booleanLiteral(0))
                statement(1);
        });
    }
}