package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiClass;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class FluentSetter implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
        // the(method(0)).method.startsWith("set");
    }

    @Override
    @Leonidas(PsiClass.class)
    public void matcher() {
        new Template(() -> {
            class A {
                void method0() {
                    anyNumberOf(statement(1));
                }
            }
        });
    }

    @Override
    @Leonidas(PsiClass.class)
    public void replacer() {
        new Template(() -> {
            class A {
                A method0() {
                    anyNumberOf(statement(1));

                    return this;
                }
            }
        });
    }
}
