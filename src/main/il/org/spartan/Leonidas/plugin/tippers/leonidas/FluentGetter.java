package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyBlock;

/**
 * @author Sharon
 * @since 13.5.17
 */
public class FluentGetter implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
        // method(0).startsWith("get"); TODO: fix
    }

    @Override
    @Leonidas(PsiMethod.class)
    public void matcher() {
        new Template(() -> new ReplacerMethod() {
            @Override
            public Object method0() {
                anyBlock(1);

                return this;
            }
        });
    }

    @Override
    @Leonidas(PsiMethod.class)
    public void replacer() {
        new Template(() -> new ReplacerMethod() {
            @Override
            public Object method0() {
                anyBlock(1);

                return this;
            }
        });
    }

    private abstract class MatcherMethod {
        public abstract void method0();
    }

    private abstract class ReplacerMethod {
        public abstract Object method0();
    }
}
