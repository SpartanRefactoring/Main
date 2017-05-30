package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.*;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
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
    private List<Matcher> fieldsMatcher, methodsMatcher, innerClassesMatcher;

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
        c.fieldsMatcher = Arrays.stream(az.classDeclaration(e.getInner()).getAllFields()).map(f -> new Matcher(Utils.wrapWithList(Pruning.prune(Encapsulator.buildTreeFromPsi(f), map)), map)).collect(Collectors.toList());
        c.methodsMatcher = Arrays.stream(az.classDeclaration(e.getInner()).getAllMethods()).map(m -> new Matcher(Utils.wrapWithList(Pruning.prune(Encapsulator.buildTreeFromPsi(m), map)), map)).collect(Collectors.toList());
        c.innerClassesMatcher = Arrays.stream(az.classDeclaration(e.getInner()).getAllInnerClasses()).map(ic -> new Matcher(Utils.wrapWithList(Pruning.prune(Encapsulator.buildTreeFromPsi(ic), map)), map)).collect(Collectors.toList());
        return c;
    }

    private void getAllPermiutation(List<Integer[]> l, int location, List<Integer> remains, Integer[] prefix) {
        if (location < 0) l.add(prefix.clone());
        for (Integer remain : remains) {
            List<Integer> newRemains = new LinkedList<>(remains);
            prefix[location] = remain;
            newRemains.remove(remain);
            getAllPermiutation(l, location - 1, newRemains, prefix);
        }
    }

    @Override
    public MatchingResult generalizes(Encapsulator e) {
        if (!iz.classDeclaration(e.inner)) return new MatchingResult(false);
        PsiClass c = az.classDeclaration(e.inner);
        PsiField[] fields = c.getFields();
        PsiMethod[] methods = c.getMethods();
        PsiClass[] innerClasses = c.getInnerClasses();
        List<Integer[]> fieldsPermutation = new LinkedList<>();
        List<Integer[]> methodsPermutation = new LinkedList<>();
        List<Integer[]> innerClassesPermutation = new LinkedList<>();
        getAllPermiutation(fieldsPermutation, fields.length - 1, IntStream.range(0, fields.length).boxed().collect(Collectors.toList()), new Integer[fields.length]);
        getAllPermiutation(methodsPermutation, methods.length - 1, IntStream.range(0, methods.length).boxed().collect(Collectors.toList()), new Integer[methods.length]);
        getAllPermiutation(innerClassesPermutation, innerClasses.length - 1, IntStream.range(0, innerClasses.length).boxed().collect(Collectors.toList()), new Integer[innerClasses.length]);
        boolean b = super.generalizes(e).matches() && fieldsPermutation.stream().anyMatch(perm -> {
            for (int i = 0; i < fields.length; i++) {
                if (!fieldsMatcher.get(i).match(fields[perm[i]])) {
                    return false;
                }
            }
            return true;
        });
        b = b && super.generalizes(e).matches() && methodsPermutation.stream().anyMatch(perm -> {
            for (int i = 0; i < methods.length; i++) {
                if (!methodsMatcher.get(i).match(methods[perm[i]])) {
                    return false;
                }
            }
            return true;
        });

        b = b && super.generalizes(e).matches() && innerClassesPermutation.stream().anyMatch(perm -> {
            for (int i = 0; i < innerClasses.length; i++) {
                if (!innerClassesMatcher.get(i).match(innerClasses[perm[i]])) {
                    return false;
                }
            }
            return true;
        });
        return new MatchingResult(b);
    }

    @Override
    public void replaceByRange(List<PsiElement> elements, PsiRewrite r) {
        PsiClass c = az.classDeclaration(elements.get(0));
        if (iz.type(inner)){
            super.replaceByRange(Utils.wrapWithList(JavaPsiFacade.getElementFactory(Utils.getProject()).createTypeElementFromText(c.getName(), c)), r);
        } else {
            super.replaceByRange(elements, r);
        }
    }
}
