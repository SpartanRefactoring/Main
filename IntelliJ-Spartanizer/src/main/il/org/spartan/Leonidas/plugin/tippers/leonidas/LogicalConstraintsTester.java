package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiDeclarationStatement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * @author Sharon
 */
public class LogicalConstraintsTester implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
        element(1).asBooleanExpression.mustBeLiteral();
    }

    @Override
    @Leonidas(PsiDeclarationStatement.class)
    public void matcher() {
        new Template(() -> {
            boolean b = booleanExpression(1);
        });
    }

    @Override
    @Leonidas(PsiDeclarationStatement.class)
    public void replacer() {
        new Template(() -> {
            boolean b = booleanExpression(1) && true;
        });
    }
}
