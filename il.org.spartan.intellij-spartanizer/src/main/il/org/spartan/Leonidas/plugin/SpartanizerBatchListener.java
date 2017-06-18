package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

/**
 * @author RoeiRaz
 * @since 18/06/17
 */
public interface SpartanizerBatchListener {
    void onInvoke(SpartanizerBatch batch);

    void onTip(SpartanizerBatch batch, PsiElement element, Tipper t);
}
