package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * A basic block representing a Variable : identifier or field access.
 * @author Amir Sagiv
 * @Date 24-05-2017
 */
public class Variable extends NamedElement {
    public static final String TEMPLATE = "variable";


    public Variable(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Variable() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return !iz.referenceExpression(e) ? null : az.referenceExpression(e).getText();
    }

    @Override
    public boolean conforms(PsiElement e) {
        return iz.referenceExpression(e) && super.conforms(e);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return new MatchingResult(super.generalizes(e, m).matches() && iz.referenceExpression(e.getInner()));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new Variable(e);
    }

}
