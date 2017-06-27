package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A basic block representing a generic class.
 * For example "class Class0 {...}
 * @author amirsagiv83, michalcohen
 * @since 29-05-2017.
 */
public class Class extends NamedElement{

    private static final String TEMPLATE = "Class";
    List<Encapsulator> fields, methods, innerClasses;
    private List<Matcher> fieldsMatchers, methodsMatchers, innerClassesMatchers;

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
    protected String getName(PsiElement ¢) {
        return !iz.classDeclaration(¢) ? null : az.classDeclaration(¢).getName();
    }

    @Override
    public boolean conforms(PsiElement ¢) {
        return iz.classDeclaration(¢) && super.conforms(¢);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        Class $ = new Class(e);
        $.fields = Arrays.stream(az.classDeclaration(e.getInner()).getFields()).map(λ -> Pruning.prune(Encapsulator.buildTreeFromPsi(λ), m)).collect(Collectors.toList());
        $.methods = Arrays.stream(az.classDeclaration(e.getInner()).getMethods()).map(λ -> Pruning.prune(Encapsulator.buildTreeFromPsi(λ), m)).collect(Collectors.toList());
        $.innerClasses = Arrays.stream(az.classDeclaration(e.getInner()).getInnerClasses()).map(λ -> Pruning.prune(Encapsulator.buildTreeFromPsi(λ), m)).collect(Collectors.toList());
        $.fieldsMatchers = $.fields.stream().map(λ -> new Matcher(Utils.wrapWithList(λ), m)).collect(Collectors.toList());
        $.methodsMatchers = $.methods.stream().map(λ -> new Matcher(Utils.wrapWithList(λ), m)).collect(Collectors.toList());
        $.innerClassesMatchers = $.innerClasses.stream().map(ic -> new Matcher(Utils.wrapWithList(ic), m)).collect(Collectors.toList());
        return $;
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        if (!iz.classDeclaration(e.inner)) return new MatchingResult(false);
        PsiClass c = az.classDeclaration(e.inner);
        MatchingResult $ = new MatchingResult(true);
        if (!super.generalizes(e, m).matches())
            return new MatchingResult(false);
        $.combineWith(matchInnerElements(c.getFields(), fieldsMatchers));
        $.combineWith(matchInnerElements(c.getMethods(), methodsMatchers));
        $.combineWith(matchInnerElements(c.getInnerClasses(), innerClassesMatchers));
        return $;
    }

    /**
     * @param innerElements the element of the class of the user
     * @param ms      the matchers of the inner elements (methods, fields or inner classes) of the template.
     * @return A matching result for matching the elements of the user with the templates, regardless of order.
     */
    private MatchingResult matchInnerElements(PsiElement[] innerElements, List<Matcher> ms){
        if (ms.isEmpty()) return new MatchingResult(true);
        List<List<MatchingResult>> l = ms.stream().map(m -> Arrays.stream(innerElements).map(ie -> m.getMatchingResult(ie, new Wrapper<>(0))).filter(mr -> mr.matches()).collect(Collectors.toList())).collect(Collectors.toList());
        MatchingResult[] ass = new MatchingResult[ms.size()];
        if (!matchInnerElementAux(l, ms.size() - 1, new LinkedList<>(), ass))
			return new MatchingResult(false);
        MatchingResult $ = new MatchingResult(true);
        Arrays.stream(ass).forEach($::combineWith);
        return $;
    }

    private boolean matchInnerElementAux(List<List<MatchingResult>> rss, int i, List<MatchingResult> used, MatchingResult[] ass){
        if (i < 0) return true;
        for (MatchingResult mr : rss.get(i)){
            if (used.contains(mr)) continue;
            used.add(mr);
            ass[i] = mr;
            if (matchInnerElementAux(rss, i - 1, used, ass))
                return true;
            used.remove(mr);
        }
        return false;
    }

    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> es, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        PsiClass psiClass = az.classDeclaration(es.get(0));
        PsiClass innerAsClass = az.classDeclaration(inner);
        innerAsClass.setName(psiClass.getName());
        List<Encapsulator> methods = Arrays.stream(innerAsClass.getMethods()).map(λ -> Pruning.prune(Encapsulator.buildTreeFromPsi(λ), null)).collect(Collectors.toList());
        List<Encapsulator> fields = Arrays.stream(innerAsClass.getFields()).map(λ -> Pruning.prune(Encapsulator.buildTreeFromPsi(λ), null)).collect(Collectors.toList());
        List<Encapsulator> innerClasses = Arrays.stream(innerAsClass.getInnerClasses()).map(λ -> Pruning.prune(Encapsulator.buildTreeFromPsi(λ), null)).collect(Collectors.toList());
        List<Encapsulator> prunedChildren = new LinkedList<>();
        prunedChildren.addAll(methods);
        prunedChildren.addAll(fields);
        prunedChildren.addAll(innerClasses);
        prunedChildren.forEach(c -> c.accept(n -> {
            if (!n.isGeneric()) return;
            GenericEncapsulator ge = az.generic(n);
            ge.replaceByRange(m.get(ge.getId()), m, r);
        }));
        return Utils.wrapWithList(inner);
    }
}
