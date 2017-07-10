package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.EncapsulatorIterator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericEncapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Quantifier;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A class responsible for the logic of matching the tree of the user to the definition of the tipper and extracting the
 * correct information of the tree of the user for the sake of future replacing.
 * It is based on the algorithm of SNOBOL4 language pattern matching, and is consistent with the definitions of the beads
 * diagram.
 *
 * @author michalcohen
 * @since 31-03-2017.
 */
public class Matcher {

    private final Map<Integer, List<StructuralConstraint>> constrains = new HashMap<>();
    private List<Encapsulator> roots;

    Matcher() {
        roots = null;
    }

    public Matcher(List<Encapsulator> r, Map<Integer, List<Constraint>> map) {
        roots = r;
        buildMatcherTree(this, map);
    }

    /**
     * @param treeToMatch the tree of the user
     * @return true iff the tree of the user matcher the roots and holds through all the constraints.
     */
    public boolean match(PsiElement treeToMatch) {
        return getMatchingResult(treeToMatch, new Wrapper<>(0), new MatchingResult(true)).matches();
    }

    /**
     * @param treeToMatch the tree of the user
     * @return the matching result: true iff the trees match, and a mapping between ids of
     * generic element to the element of the user.
     */
    public MatchingResult getMatchingResult(PsiElement treeToMatch, Wrapper<Integer> numberOfNeighbors, MatchingResult previousRes) {
        MatchingResult mr = new MatchingResult(false);
        List<Encapsulator> potentialRoots = new ArrayList<>();
        PsiElement ce = treeToMatch;
        int n = Utils.getNumberOfRootsPossible(treeToMatch);
        for (int i = 1; i <= n; ++i) {
            MatchingResult m = new MatchingResult(true);
            potentialRoots.add(Encapsulator.buildTreeFromPsi(ce));
            ce = Utils.getNextActualSibling(ce);
            previousRes.setMatches();
            m.combineWith(matchingRecursion(
                    new EncapsulatorIterator(roots), new EncapsulatorIterator(potentialRoots), previousRes, i == n));
            if (m.matches()) {
                mr.combineWith(m);
                mr.setMatches();
                numberOfNeighbors.set(i);
                break;
            }
        }
        if (mr.notMatches()) return mr;

        Map<Integer, List<PsiElement>> info = mr.getMap();
        return info.keySet().stream()
                .allMatch(id -> {
                    Boolean b1 = constrains.getOrDefault(id, new LinkedList<>()).stream()
                            .allMatch(c -> info.get(id).stream().peek(x -> {
                            }).allMatch(e -> c.match(e, mr)));
                    Boolean b2 = getGenericElements().get(id) == null
                            || getGenericElements().get(id).getConstraints().stream().allMatch(c -> info.get(id)
                            .stream().allMatch(e -> c.accept(new Encapsulator(e), info)));
                    return b1 && b2;
                })
                ? mr : mr.setNotMatches();
    }


    /**
     * Goes through each and every quantifier and tries all possible assignments for this quantifiers.
     *
     * @return MatchingResult false if the trees don't match and MatchingResult true with mapping between identifier
     * of generic elements to its instances if the trees match.
     */
    @UpdatesIterator
    private MatchingResult matchingRecursion(EncapsulatorIterator needle, EncapsulatorIterator cursor,
                                             MatchingResult r, Boolean allElementsIncluded) {
        EncapsulatorIterator bgNeedle = needle.clone(), bgCursor = cursor.clone(); // base ground iterators
        r.combineWith(matchBead(bgNeedle, bgCursor, r));
        if (r.notMatches()) return r;
        if (!bgNeedle.hasNext() && !bgCursor.hasNext()) return r.setMatches();
        if (!bgCursor.hasNext()) {
            while (bgNeedle.hasNext()) {
                if (!iz.quantifier(bgNeedle.value()))
                    return r.setNotMatches();
                Quantifier q = az.quantifier(bgNeedle.value());
                if (iz.quantifierAll(q) && !allElementsIncluded)
                    return r.setNotMatches();
                Quantifier.QuantifierIterator it = q.quantifierIterator(bgCursor, r.getMap());
                if (it.value() != 0) return r.setNotMatches();
                bgNeedle.setNumberOfOccurrences(0);
                bgNeedle.next();
            }
            return r.setMatches();
        }
        if (bgNeedle.hasNext() != bgCursor.hasNext() && iz.quantifier(bgNeedle.value())) return r.setNotMatches();
        EncapsulatorIterator varNeedle, varCursor; // variant iterator for each attempt to match quantifier
        if (!iz.quantifier(bgNeedle.value()))
            return r.setNotMatches();
        Quantifier q = az.quantifier(bgNeedle.value());
        if (iz.quantifierAll(q) && !allElementsIncluded)
            return r.setNotMatches();
        for (Quantifier.QuantifierIterator it = q.quantifierIterator(bgCursor, r.getMap()); it.hasNext(); it.next()) {
            r.setMatches();
            varNeedle = bgNeedle.clone();
            varCursor = bgCursor.clone();
            varNeedle.setNumberOfOccurrences(it.value());
            MatchingResult mq = matchQuantifier(varNeedle, varCursor, r);
            if (mq.notMatches())
                continue;
            MatchingResult sr = matchingRecursion(varNeedle, varCursor, r, allElementsIncluded);
            if (sr.matches())
                return r.combineWith(mq).combineWith(sr);
        }
        return r.setNotMatches();
    }

    /**
     * Matches all the elements until the next quantifier or the end of the tree
     *
     * @return MatchingResult false if the sections don't match and MatchingResult true with mapping between identifier
     * of generic elements in this section to its instances if the sections match.
     */
    @UpdatesIterator
    private MatchingResult matchBead(EncapsulatorIterator needle, EncapsulatorIterator cursor, MatchingResult r) {
        for (; needle.hasNext() && cursor.hasNext() && !iz.quantifier(needle.value()); needle.next(), cursor.next()) {
            MatchingResult ic = iz.conforms(cursor.value(), needle.value(), r.getMap());
            if (ic.notMatches() || (needle.hasNext() != cursor.hasNext()))
                return r.setNotMatches();
            r.combineWith(ic);
            if (needle.value().isGeneric()) {
                if (!r.getMap().containsKey(az.generic(needle.value()).getId()))
                    r.put(az.generic(needle.value()).getId(), cursor.value().getInner());
                else if (r.getMap().get(az.generic(needle.value()).getId()).stream()
                        .noneMatch(e -> e.getText().equals(cursor.value().getText())))
                    return r.setNotMatches();
                cursor.matchedWithGeneric();
            }
        }
        return r;
    }

    /**
     * Matches all the occurrences of a quantifier according to its NumberOfOccurrences.
     *
     * @return MatchingResult false if the sections don't match and MatchingResult true with mapping between identifier
     * of generic elements in this section to its instances if the sections match.
     */
    @UpdatesIterator
    private MatchingResult matchQuantifier(EncapsulatorIterator needle, EncapsulatorIterator cursor
            , MatchingResult r) {
        // MatchingResult m = new MatchingResult(true);
        int n = needle.getNumberOfOccurrences();
        if (n == 0) {
            needle.next();
            return r.setMatches();
        }
        Quantifier q = az.quantifier(needle.value());
        for (int i = 0; i < n; needle.next(), cursor.next(), ++i) {
            MatchingResult ic = iz.conforms(cursor.value(), needle.value(), r.getMap());
            if (ic.notMatches() || (cursor.hasNext() ^ needle.hasNext()))
                return r.setNotMatches();
            r.combineWith(ic);
            if (needle.value().isGeneric()) {
                r.put(az.generic(needle.value()).getId(), cursor.value().getInner());
                cursor.matchedWithGeneric();
            }
        }
        while (needle.value() == q && needle.hasNext())
            needle.next();
        return r;
    }

    /**
     * @param m   builds recursively the matchers for the constraints that are relevant to the current matcher.
     * @param map a mapping between id of generic elements and lists of constraints.
     */
    private void buildMatcherTree(Matcher m, Map<Integer, List<Constraint>> map) {
        if (map == null) return;
        m.getGenericElementsWithNoFields()
                .forEach((i, e) -> java.util.Optional.ofNullable(map.get(i)).ifPresent(z -> z.forEach(j -> {
                    if (j instanceof StructuralConstraint)
                        m.addConstraint(i, (StructuralConstraint) j);
                    else {
                        NonStructuralConstraint nsc = (NonStructuralConstraint) j;
                        Encapsulator ie = !iz.quantifier(e) ? e : az.quantifier(e).getInternal();
                        try {
                            Utils.getDeclaredMethod(ie.getClass(), nsc.methodName, Arrays.stream(nsc.objects).map(Object::getClass).collect(Collectors.toList()).toArray(new Class<?>[]{})).invoke(ie, nsc.objects);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                            e1.printStackTrace();
                        }
                    }
                })));
        m.getConstraintsMatchers().forEach(im -> buildMatcherTree(im, map));
    }

    private void setRoots(List<Encapsulator> es) {
        roots = es;
    }

    /**
     * Adds a constraint on a generic element inside the tree of the roots.
     *
     * @param id - the id of the element that we constraint.
     * @param c  - the constraint
     */
    private void addConstraint(Integer id, StructuralConstraint c) {
        constrains.putIfAbsent(id, new LinkedList<>());
        constrains.get(id).add(c);
    }

    /**
     * @return the matcher elements in all the constraints applicable on the roots of this matcher.
     */
    private List<Matcher> getConstraintsMatchers() {
        return constrains.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream()
                .map(StructuralConstraint::getMatcher)
                .collect(Collectors.toList());
    }


    /**
     * @param treeToMatch - The patterns from which we extract the IDs
     * @return a mapping between an ID to a PsiElement
     */
    public Map<Integer, List<PsiElement>> extractInfo(PsiElement treeToMatch, Wrapper<Integer> numberOfNeighbors) {
        return getMatchingResult(treeToMatch, numberOfNeighbors, new MatchingResult(true)).getMap();
    }

    /**
     * @return list of Ids of all the generic elements in the tipper.
     */
    private Map<Integer, GenericEncapsulator> getGenericElements() {
        final Map<Integer, GenericEncapsulator> tmp = new HashMap<>();
        roots.forEach(root -> root.accept(e -> {
            if (e.isGeneric()) {
                tmp.put(az.generic(e).getId(), (GenericEncapsulator) e);
                tmp.putAll(az.generic(e).getGenericElements());
            }
        }));
        return tmp;
    }

    /**
     * @return list of Ids of all the generic elements in the tipper.
     */
    private Map<Integer, GenericEncapsulator> getGenericElementsWithNoFields() {
        final Map<Integer, GenericEncapsulator> tmp = new HashMap<>();
        roots.forEach(root -> root.accept(e -> {
            if (e.isGeneric())
                tmp.put(az.generic(e).getId(), (GenericEncapsulator) e);
        }));
        return tmp;
    }

    public List<Encapsulator> getAllRoots() {
        return roots;
    }

    private boolean match(PsiElement e, MatchingResult mr) {
        return getMatchingResult(e, new Wrapper<>(0), mr).matches();
    }

    public abstract static class Constraint {
        protected final ConstraintType type;

        Constraint(ConstraintType t) {
            type = t;
        }

        public enum ConstraintType {
            IS, ISNOT, SPECIFIC
        }
    }

    /**
     * Represents a constraint on a generalized variable of the leonidas language.
     *
     * @author michalcohen
     * @since 01-04-2017.
     */
    public static class StructuralConstraint extends Constraint {

        private final Matcher matcher;

        public StructuralConstraint(ConstraintType t, List<Encapsulator> e) {
            super(t);
            matcher = new Matcher();
            matcher.setRoots(e);
        }

        public Matcher getMatcher() {
            return matcher;
        }

        /**
         * @param e the users tree to match.
         * @param mr
         * @return indication of e being matched recursively to the matcher, when taking in consideration the type of the constraint.
         */
        boolean match(PsiElement e, MatchingResult mr) {
            MatchingResult nmr = new MatchingResult(true).combineWith(mr);
            return (type == ConstraintType.IS && matcher.match(e, nmr)) || (type == ConstraintType.ISNOT && !matcher.match(e, nmr));
        }
    }

    public static class NonStructuralConstraint extends Constraint {
        Object[] objects;
        Encapsulator element;
        String methodName;

        public NonStructuralConstraint(String methodName, Object[] o) {
            super(ConstraintType.SPECIFIC);
            this.methodName = methodName;
            objects = o;
        }

        public void setElement(Encapsulator e) {
            element = e;
        }
    }
}

