package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author amirsagiv83, michalcohen
 * @since 29-05-2017.
 */
public class Class extends NamedElement{

    private static final String TEMPLATE = "Class";
    private List<Matcher> fieldsMatchers, methodsMatchers, innerClassesMatchers;
    List<Encapsulator> fields, methods, innerClasses;

    public Class(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Class() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return iz.classDeclaration(e) ? az.classDeclaration(e).getName() : null;
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        Class c = new Class(e);
        c.fields = Arrays.stream(az.classDeclaration(e.getInner()).getFields()).map(f -> Pruning.prune(Encapsulator.buildTreeFromPsi(f), map)).collect(Collectors.toList());
        c.methods = Arrays.stream(az.classDeclaration(e.getInner()).getMethods()).map(f -> Pruning.prune(Encapsulator.buildTreeFromPsi(f), map)).collect(Collectors.toList());
        c.innerClasses = Arrays.stream(az.classDeclaration(e.getInner()).getInnerClasses()).map(f -> Pruning.prune(Encapsulator.buildTreeFromPsi(f), map)).collect(Collectors.toList());
        c.fieldsMatchers = c.fields.stream().map(f -> new Matcher(Utils.wrapWithList(f), map)).collect(Collectors.toList());
        c.methodsMatchers = c.methods.stream().map(m -> new Matcher(Utils.wrapWithList(m), map)).collect(Collectors.toList());
        c.innerClassesMatchers = c.innerClasses.stream().map(ic -> new Matcher(Utils.wrapWithList(ic), map)).collect(Collectors.toList());
        return c;
    }

    private void getAllPermutation(List<Integer[]> l, int location, List<Integer> remains, Integer[] prefix) {
        if (location < 0) l.add(prefix.clone());
        for (Integer remain : remains) {
            List<Integer> newRemains = new LinkedList<>(remains);
            prefix[location] = remain;
            newRemains.remove(remain);
            getAllPermutation(l, location - 1, newRemains, prefix);
        }
    }

    @Override
    public MatchingResult generalizes(Encapsulator e) {
        if (!iz.classDeclaration(e.inner)) return new MatchingResult(false);
        PsiClass c = az.classDeclaration(e.inner);
        PsiField[] fieldsOfInstance = c.getFields();
        PsiMethod[] methodsOfInstance = c.getMethods();
        PsiClass[] innerClassesOfInstance = c.getInnerClasses();
        List<Integer[]> fieldsPermutation = new LinkedList<>();
        List<Integer[]> methodsPermutation = new LinkedList<>();
        List<Integer[]> innerClassesPermutation = new LinkedList<>();
        getAllPermutation(fieldsPermutation, fields.size() - 1, IntStream.range(0, fields.size()).boxed().collect(Collectors.toList()), new Integer[fields.size()]);
        getAllPermutation(methodsPermutation, methods.size() - 1, IntStream.range(0, methods.size()).boxed().collect(Collectors.toList()), new Integer[methods.size()]);
        getAllPermutation(innerClassesPermutation, innerClasses.size() - 1, IntStream.range(0, innerClasses.size()).boxed().collect(Collectors.toList()), new Integer[innerClasses.size()]);
        MatchingResult mr = new MatchingResult(true);
        if (!super.generalizes(e).matches())
            return new MatchingResult(false);
        mr.combineWith(matchInnerElement(fieldsPermutation, fieldsOfInstance, fieldsMatchers));
        mr.combineWith(matchInnerElement(methodsPermutation, methodsOfInstance, methodsMatchers));
        mr.combineWith(matchInnerElement(innerClassesPermutation, innerClassesOfInstance, innerClassesMatchers));
        return mr;
    }

    private MatchingResult matchInnerElement(List<Integer[]> permutations, PsiElement[] innerElements, List<Matcher> matchers) {
        if (permutations.size() == 1 && permutations.get(0).length == 0) return new MatchingResult(true);
        Wrapper<Integer> dummy = new Wrapper<>(0);
        return permutations.stream().map(perm -> {
            MatchingResult mr = new MatchingResult(false);
            for (int i = 0; i < innerElements.length; i++) {
                MatchingResult imr = matchers.get(i).getMatchingResult(innerElements[perm[i]], dummy);
                if (imr.notMatches()) {
                    return imr;
                }
                mr.combineWith(imr);
                mr.setMatches();
            }
            return mr;
        }).filter(mr -> mr.matches()).findFirst().orElse(new MatchingResult(false));
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> elements, Map<Integer, List<PsiElement>> map, PsiRewrite r) {
        PsiClass psiClass = az.classDeclaration(elements.get(0));
        PsiClass innerAsClass = az.classDeclaration(inner);
        innerAsClass.setName(psiClass.getName());
        List<Encapsulator> methods = Arrays.stream(innerAsClass.getMethods()).map(m -> Pruning.prune(Encapsulator.buildTreeFromPsi(m), null)).collect(Collectors.toList());
        List<Encapsulator> fields = Arrays.stream(innerAsClass.getFields()).map(f -> Pruning.prune(Encapsulator.buildTreeFromPsi(f), null)).collect(Collectors.toList());
        List<Encapsulator> innerClasses = Arrays.stream(innerAsClass.getInnerClasses()).map(m -> Pruning.prune(Encapsulator.buildTreeFromPsi(m), null)).collect(Collectors.toList());
        List<Encapsulator> prunedChildren = new LinkedList<>();
        prunedChildren.addAll(methods);
        prunedChildren.addAll(fields);
        prunedChildren.addAll(innerClasses);
        prunedChildren.forEach(c -> c.accept(n -> {
            if (!n.isGeneric()) return;
            GenericEncapsulator ge = az.generic(n);
            ge.replaceByRange(map.get(ge.getId()), map, r);
        }));
        return Utils.wrapWithList(inner);
    }
}
