package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

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
        super();
    }

    @Override
    public boolean conforms(PsiElement other) {
        return iz.method(other) && az.method(other).getName().startsWith(TEMPLATE);
    }

    @Override
    public Integer extractId(PsiElement e) {
        return Integer.parseInt(az.method(e).getName().substring(TEMPLATE.length()));
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        if (!super.generalizes(e) || !iz.method(e.getInner())) return false;
        PsiMethod m = az.method(e.getInner());

        return matcherReturnType.match(m.getReturnTypeElement()) &&
                matcherParameters.match(m.getParameterList()) &&
                matcherCodeBlock.match(m.getBody());
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
