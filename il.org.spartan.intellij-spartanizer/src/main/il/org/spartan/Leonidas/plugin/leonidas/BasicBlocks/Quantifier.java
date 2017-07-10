package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * @author michalcohen
 * @since 01-07-2017.
 */
public interface Quantifier {

    boolean goUpwards(Encapsulator prev, Encapsulator next);

    MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m);

    QuantifierIterator quantifierIterator(EncapsulatorIterator bgCursor, Map<Integer, List<PsiElement>> m);

    Encapsulator getInternal();

    Integer getId();

    class QuantifierIterator implements java.util.Iterator<Integer> {
        int start, end;

        QuantifierIterator(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean hasNext() {
            return start <= end;
        }

        @Override
        public Integer next() {
            ++start;
            return start - 1;
        }

        public Integer value() {
            return start;
        }
    }
}
