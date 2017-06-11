package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * @author Amir Sagiv
 * @Date 24-05-2017
 */
public class Identifier extends NamedElement {
    public static final String TEMPLATE = "identifier";

    public Identifier(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Identifier() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return iz.identifier(e) ? az.identifier(e).getText() : null;
    }

    @Override
    public MatchingResult generalizes(Encapsulator e) {
        return new MatchingResult(super.generalizes(e).matches() && iz.identifier(e.getInner()));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Identifier(e);
    }

    /* Constraints Methods */


    public void contains(String s) {
        addConstraint(e -> az.identifier(e.inner).getText().contains(s));
    }

    public void notContains(String s) {
        addConstraint(e -> !(az.identifier(e.inner).getText().contains(s)));
    }

    public void endWith(String s) {
        addConstraint(e -> az.identifier(e.inner).getText().endsWith(s));
    }
}
