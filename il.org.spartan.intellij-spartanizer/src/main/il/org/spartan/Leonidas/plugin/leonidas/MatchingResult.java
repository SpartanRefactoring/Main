package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Simple POJO for maintaining the result of the matching process. Alongside with a boolean result stating whether
 * the matching process succeeded, we also like to maintain a mapping between the matched generic elements to
 * their occurrences.
 * @author michalcohen
 * @since 15-05-2017.
 */
public class MatchingResult {
    Map<Integer, List<PsiElement>> m = new HashMap<>();
    boolean b;

    public MatchingResult(boolean defaultRes) {
        b = defaultRes;
    }

    public boolean matches() {
        return b;
    }

    public boolean notMatches() {
        return !b;
    }

    public MatchingResult setMatches() {
        b = true;
        return this;
    }

    public MatchingResult setNotMatches() {
        b = false;
        return this;
    }
    
    public MatchingResult put(Integer i, PsiElement e) {
        m.putIfAbsent(i, new LinkedList<>());
        if (m.get(i).stream().anyMatch(me -> me == e)) return this;
        m.get(i).add(e);
        return this;
    }

    public MatchingResult combineWith(MatchingResult mr) {
        if (!mr.b) {
            b = false;
            return this;
        }
        mr.m.forEach((k, v) -> m.put(k, v));
        return this;
    }

    public Map<Integer, List<PsiElement>> getMap() {
        return m;
    }
}
