package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Map;

/**
 * @author michalcohen
 * @since 20-06-2017.
 */
public abstract class AnyNumberOfBasedNames extends QuantifierBasedNames {

    public AnyNumberOfBasedNames(Encapsulator e, String template, Encapsulator i) {
        super(e, template, i);
        internal = i;
    }

    public AnyNumberOfBasedNames(String template) {
        super(template);
    }

    @Override
    public QuantifierIterator quantifierIterator(EncapsulatorIterator bgCursor, Map<Integer, List<PsiElement>> m) {
        return new QuantifierIterator(0, getNumberOfOccurrences(bgCursor, m));
    }
}
