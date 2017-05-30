package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;


/**
 * @author Sharon
 * @since 13.5.17
 */
public class Method extends ModifiableElement {
    private static final String TEMPLATE = "method";
    private Matcher matcherReturnType, matcherParameters, matcherCodeBlock;

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
        m.matcherReturnType = new Matcher(Utils.wrapWithList(Encapsulator.buildTreeFromPsi(az.method(e.getInner()).getReturnTypeElement())), map);
        m.matcherParameters = new Matcher(Utils.wrapWithList(Encapsulator.buildTreeFromPsi(az.method(e.getInner()).getParameterList())), map);
        m.matcherCodeBlock = new Matcher(Utils.wrapWithList(Encapsulator.buildTreeFromPsi(az.method(e.getInner()).getBody())), map);
        return m;
    }
}
