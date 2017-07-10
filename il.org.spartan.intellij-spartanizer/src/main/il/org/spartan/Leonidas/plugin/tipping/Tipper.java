package il.org.spartan.Leonidas.plugin.tipping;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oren Afek
 * @author Michal Cohen
 * @since 01-12-2016
 */
public interface Tipper {

    boolean canTip(PsiElement e);

    @NotNull
    default String description() {
        return getClass().getSimpleName();
    }

    @NotNull
    String description(PsiElement t);

    @NotNull
    String name();

    @NotNull
    Tip tip(PsiElement node);

    @NotNull
    Class<? extends PsiElement> getOperableType();

    @NotNull
    default Map<String, String> getExamples() {
        return new HashMap<>();
    }
}
