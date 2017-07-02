package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * A basic block representing a field declaration: public Class0 field0;
 * @author Sharon LK
 * @since 23-06-2017 (No correct date was given...)
 * @TODO: fix initializer.
 */
public class FieldDeclaration extends ModifiableElement {
    public static final String TEMPLATE = "field";

    public FieldDeclaration(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected FieldDeclaration() {
        super(TEMPLATE);
    }

    @Override
    public boolean conforms(PsiElement other) {
        return iz.fieldDeclaration(other) && super.conforms(other);
    }

    @Override
    public Integer extractId(PsiElement e) {
        return Integer.parseInt(az.fieldDeclaration(e).getName().substring(TEMPLATE.length()));
    }

    @Override
    protected String getName(PsiElement e) {
        return !iz.fieldDeclaration(e) ? null : az.fieldDeclaration(e).getName();
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.fieldDeclaration(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new FieldDeclaration(e);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return super.generalizes(e, m).combineWith(new MatchingResult(iz.fieldDeclaration(e.getInner())));
    }
}
