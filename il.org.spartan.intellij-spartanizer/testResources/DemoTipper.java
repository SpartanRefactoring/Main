package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Statement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.the;

/**
 * @author michalcohen
 * @since 29-04-2017.
 */
public class DemoTipper implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        the(0).isNot(() -> {
            if(booleanExpression(3)){
                statement(4);
            }
        }).ofType(PsiIfStatement.class);
        the(0).nameStartsWith("get");
        the(0).isPublic();
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void matcher() {
        new Template(() -> {
            if (cond) {
                statement(first);
                statement(second);
            }
        });
    }

    @Override
    @Leonidas(PsiIfStatement.class)
    public void replacer() {
        new Template(() -> {
            void method0() {
              anyNumberOf(statement(1));
            }
        });
    }
}