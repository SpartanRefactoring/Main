package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import org.jetbrains.annotations.NotNull;

/**
 * @author melanyc
 * @since 20-01-2017.
 */
public class NoTip implements Tipper {
    @Override
    public boolean canTip(PsiElement e) {
        return true;
    }

    @NotNull
    @Override
    public String name() {
        return "NoTip";
    }

    @NotNull
    @Override
    public String description(PsiElement e) {
        return "";
    }

    @NotNull
    @Override
    public Tip tip(PsiElement node) {
        return new Tip("", node, NoTip.class) {
            @Override
            public void go(PsiRewrite r) {
            }
        };
    }

    @NotNull
    @Override
    public Class<? extends PsiElement> getOperableType() {
        return PsiElement.class;
    }
}
