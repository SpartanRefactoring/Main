package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;
import il.org.spartan.Leonidas.plugin.leonidas.Replacer;

import java.util.List;
import java.util.Map;


/**
 * @author Sharon
 * @since 13.5.17
 */
public class Method extends ModifiableElement {
    private static final String TEMPLATE = "method";
    private Matcher matcherReturnType, matcherParameters, matcherCodeBlock;
    private Replacer replacerReturnType, replacerParameters, replacerCodeBlock;

    public Method(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Method() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return iz.method(e) ? az.method(e).getName() : null;
    }

    @Override
    public MatchingResult generalizes(Encapsulator e) {
        if (super.generalizes(e).notMatches() || !iz.method(e.getInner())) return new MatchingResult(false);
        PsiMethod m = az.method(e.getInner());
        Wrapper<Integer> dummy = new Wrapper<>(0);
        return matcherReturnType.getMatchingResult(m.getReturnTypeElement(), dummy).combineWith(
                matcherParameters.getMatchingResult(m.getParameterList(), dummy)).combineWith(
                matcherCodeBlock.getMatchingResult(m.getBody(), dummy));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return next != null && iz.method(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        Method m = new Method(e);
        Encapsulator returnType = Pruning.prune(Encapsulator.buildTreeFromPsi(az.method(e.getInner()).getReturnTypeElement()), map);
        Encapsulator parameters = Pruning.prune(Encapsulator.buildTreeFromPsi(az.method(e.getInner()).getParameterList()), map);
        Encapsulator codeBlock = Pruning.prune(Encapsulator.buildTreeFromPsi(az.method(e.getInner()).getBody()), map);
        m.matcherReturnType = new Matcher(Utils.wrapWithList(returnType), map);
        m.matcherParameters = new Matcher(Utils.wrapWithList(parameters), map);
        m.matcherCodeBlock = new Matcher(Utils.wrapWithList(codeBlock), map);
        m.replacerReturnType = new Replacer(Utils.wrapWithList(returnType));
        m.replacerParameters = new Replacer(Utils.wrapWithList(parameters));
        m.replacerCodeBlock = new Replacer(Utils.wrapWithList(codeBlock));
        return m;
    }


    @Override
    public List<PsiElement> replaceByRange(List<PsiElement> elements, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        PsiMethod e = az.method(elements.get(0));
        az.method(inner).setName(e.getName());
        replacerReturnType.replaceSingleRoot(e.getReturnTypeElement(), m, r);
        replacerParameters.replaceSingleRoot(e.getParameterList(), m, r);
        replacerCodeBlock.replaceSingleRoot(e.getBody(), m, r);
        return Utils.wrapWithList(inner);
    }
}
