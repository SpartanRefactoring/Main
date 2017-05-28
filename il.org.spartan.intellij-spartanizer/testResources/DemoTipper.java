package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiIfStatement;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Statement;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;
import static il.org.spartan.Leonidas.plugin.leonidas.The.the;

/**
 * @author michalcohen
 * @since 29-04-2017.
 */
public class DemoTipper implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
       element(1).asMethod.stratsWith("set");
       element(3).asBooleanExpression.mustBeLiteral();
       element(4).isNot(() - {
           return;
       })

    }

    @Override
     public void matcher() {
        new Template(() -> {
            class Type0 {
                void method1(Type2 id3){
                    anyNumberOf(Statement(4));
                }
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            class Type0 {
                Type0 method1(Type2 id3){
                    anyNumberOf(Statement(4));
                    return this;
                }
            }
        });
    }
}