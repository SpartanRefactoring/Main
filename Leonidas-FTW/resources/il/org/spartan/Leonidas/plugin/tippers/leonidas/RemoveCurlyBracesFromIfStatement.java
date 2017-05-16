package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.the;

/**
 * Remove redundant curly braces
 * author Oren Afek, Shron Kuninin, Michal Cohen
 * since 06/01/17
 */
public class RemoveCurlyBracesFromIfStatement implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        the(booleanExpression(0)).isNot(()-> !booleanExpression(2));
        the(statement(1)).is(()->{
            if(booleanExpression(3)){
                statement(5);
            }
        }).ofType(PsiIfStatement.class);
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void matcher() {
        new Template(() -> {
            if (booleanExpression(0)) {
                statement(1);
            }
        });
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void replacer() {
        new Template(() -> {
            if (booleanExpression(0))
                statement(1);
        });
    }


}
