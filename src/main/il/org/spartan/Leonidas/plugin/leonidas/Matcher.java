package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;

import java.util.*;
import java.util.stream.Collectors;


/**
 * A class responsible for the logic of matching the tree of the user to the definition of the tipper and extracting the
 * correct information of the tree of the user for the sake of future replacing.
 * It is based on the algorithm of SNOBOL4 language pattern matching, and is consistent with the definitions of the bead
 * diagram.
 * @author michalcohen
 * @since 31-03-2017.
 */
public class Matcher {

    private final Map<Integer, List<Constraint>> constrains = new HashMap<>();
    private Encapsulator root;

    public Matcher() {
        root = null;
    }

    public Matcher(Encapsulator r, Map<Integer, List<Constraint>> map) {
        root = r;
        buildMatcherTree(this, map);
    }

    /**
     * @param e the tree of the user
     * @return true iff the tree of the user matcher the root and holds through all the constraints.
     */
    public boolean match(PsiElement e) {
        MatchingResult m = scaryRecur(root.iterator(), Encapsulator.buildTreeFromPsi(e).iterator());
        if (m.notMatches()) return false;
        Map<Integer, List<PsiElement>> info = m.getMap();

        return info.keySet().stream()
                .allMatch(id -> constrains.getOrDefault(id, new LinkedList<>()).stream().allMatch(c -> info.get(id).stream().allMatch(n -> c.match(n))));
    }

    @UpdatesIterator
    private MatchingResult scaryRecur(Encapsulator.Iterator needle, Encapsulator.Iterator cursor) {
        MatchingResult m = new MatchingResult(true);
        Encapsulator.Iterator bgNeedle = needle.clone(), bgCursor = cursor.clone(); // base ground iterators
        m.combineWith(matchBead(bgNeedle, bgCursor));
        if (m.notMatches()) return m;
        if (!bgNeedle.hasNext() && !bgCursor.hasNext()) return m.setMatches();
        Encapsulator.Iterator varNeedle, varCursor; // variant iterator for each attempt to match quantifier
        if (iz.quantifier(bgNeedle.value())) {
            int n = az.quantifier(bgNeedle.value()).getNumberOfOccurrences(bgCursor);
            for (int i = 0; i < n; i++) {
                varNeedle = bgNeedle.clone();
                varCursor = bgCursor.clone();
                varNeedle.setNumberOfOccurrences(i);
                MatchingResult mq = matchQuantifier(varNeedle, varCursor);
                MatchingResult sr = scaryRecur(varNeedle, varCursor);
                if (!mq.matches() || !sr.matches()) continue;
                return m.combineWith(mq).combineWith(sr);

            }
        }
        return m.setNotMatches();
    }

    @UpdatesIterator
    private MatchingResult matchBead(Encapsulator.Iterator needle, Encapsulator.Iterator cursor) {
        MatchingResult m = new MatchingResult(true);
        for (; needle.hasNext() && cursor.hasNext() && !iz.quantifier(needle.value()); needle.next(), cursor.next()) {
            if (!iz.conforms(needle.value(), cursor.value()) || (needle.hasNext() != cursor.hasNext()))
                return m.setNotMatches();
            if (needle.value().isGeneric()) {
                m.put(az.generic(needle.value()).getId(), cursor.value().getInner());
            }
        }
        return m;
    }

    @UpdatesIterator
    private MatchingResult matchQuantifier(Encapsulator.Iterator needle, Encapsulator.Iterator cursor) {
        MatchingResult m = new MatchingResult(true);
        int n = needle.getNumberOfOccurrences();
        for (int i = 0; i < n; needle.next(), cursor.next(), i++) {
            if (!iz.conforms(needle.value(), cursor.value()) || (needle.hasNext() ^ cursor.hasNext())) {
                return m.setNotMatches();
            }
            if (needle.value().isGeneric()) {
                m.put(az.generic(needle.value()).getId(), cursor.value().getInner());
            }
        }
        return m;
    }

    /**
     * @param matcher builds recursively the matchers for the constraints that are relevant to the current matcher.
     * @param map a mapping between id of generic elements and lists of constraints.
     */
    private void buildMatcherTree(Matcher matcher, Map<Integer, List<Constraint>> map) {
        Set<Integer> l = matcher.getGenericElements();
        l.forEach(i -> java.util.Optional.ofNullable(map.get(i)).ifPresent(z -> z.forEach(j ->
                matcher.addConstraint(i, j))));
        matcher.getConstraintsMatchers().forEach(im -> buildMatcherTree(im, map));
    }

    public Encapsulator getRoot() {
        return root;
    }

    private void setRoot(Encapsulator n) {
        root = n;
    }

    /**
     * Adds a constraint on a generic element inside the tree of the root.
     *
     * @param id - the id of the element that we constraint.
     * @param c  - the constraint
     */
    private void addConstraint(Integer id, Constraint c) {
        constrains.putIfAbsent(id, new LinkedList<>());
        constrains.get(id).add(c);
    }

    /**
     * @return the matcher elements in all the constraints applicable on the root of this matcher.
     */
    private List<Matcher> getConstraintsMatchers() {
        return constrains.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream()
                .map(Constraint::getMatcher)
                .collect(Collectors.toList());
    }

    /**
     * @param treeToMatch - The patterns from which we extract the IDs
     * @return a mapping between an ID to a PsiElement
     */
    public Map<Integer, List<PsiElement>> extractInfo(PsiElement treeToMatch) {
        MatchingResult mr = scaryRecur(root.iterator(), Encapsulator.buildTreeFromPsi(treeToMatch).iterator());
        assert mr.matches();
        return mr.getMap();
    }

    /**
     * @return list of Ids of all the generic elements in the tipper.
     */
    private Set<Integer> getGenericElements() {
        final Set<Integer> tmp = new HashSet<>();
        root.accept(e -> {
            if (e.isGeneric()) {
                tmp.add(az.generic(e).getId());
            }
        });
        return tmp;
    }

    /**
     * Represents a constraint on a generalized variable of the leonidas language.
     *
     * @author michalcohen
     * @since 01-04-2017.
     */
    public static class Constraint {

        private final ConstraintType type;
        private final Matcher matcher;

        public Constraint(ConstraintType t, Encapsulator e) {
            type = t;
            matcher = new Matcher();
            matcher.setRoot(e);
        }

        public ConstraintType getType() {
            return type;
        }

        public Matcher getMatcher() {
            return matcher;
        }

        /**
         * @param e the users tree to match.
         * @return indication of e being matched recursively to the matcher, when taking in consideration the type of the constraint.
         */
        public boolean match(PsiElement e) {
            return (type == ConstraintType.IS && matcher.match(e)) || (type == ConstraintType.IS_NOT && !matcher.match(e));
        }

        public enum ConstraintType {
            IS,
            IS_NOT
        }
    }
}

