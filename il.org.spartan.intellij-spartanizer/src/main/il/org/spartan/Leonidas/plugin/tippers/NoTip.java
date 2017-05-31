package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

/**
 * @author melanyc
 * @since 20-01-2017.
 */
public class NoTip<T extends PsiElement> implements Tipper<T> {
    @Override
    public boolean canTip(PsiElement e) {
        return true;
    }

    @Override
    public String name() {
        return "NoTip";
    }

    @Override
    public String description(PsiElement e) {
        return "";
    }

    @Override
    public Tip tip(T node) {
        return new Tip("", node, NoTip.class) {
            @Override
            public void go(PsiRewrite r) {
            }
        };
    }

    @Override
    public Class<? extends T> getOperableType() {
        return null;
    }
}
