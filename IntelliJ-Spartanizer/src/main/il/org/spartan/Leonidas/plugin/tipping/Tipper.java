package il.org.spartan.Leonidas.plugin.tipping;

import com.intellij.psi.PsiElement;

import java.util.Map;

/**
 * @author Oren Afek
 * @author Michal Cohen
 * @since 01-12-2016
 */
public interface Tipper<T extends PsiElement> {

    boolean canTip(PsiElement e);

    default String description() {
        return getClass().getSimpleName();
    }

    String description(T t);

    String name();

    Tip tip(T node);

    Class<? extends T> getPsiClass();

    default Map<String,String> getExamples() {return null;}
}
