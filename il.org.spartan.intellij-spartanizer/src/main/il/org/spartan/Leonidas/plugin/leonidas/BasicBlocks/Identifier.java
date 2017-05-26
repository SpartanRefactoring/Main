package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;

/**
 * @author Amir Sagiv
 * @Date 24-05-2017
 */
public class Identifier extends GenericEncapsulator {
    public static final String TEMPLATE = "identifier";

    public Identifier(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Identifier() {
        super();
    }

    @Override
    public boolean conforms(PsiElement other) {
        return iz.identifier(other) && az.identifier(other).getText().startsWith(TEMPLATE);
    }

    @Override
    public Integer extractId(PsiElement e) {
        return Integer.parseInt(az.identifier(e).getText().substring(TEMPLATE.length()));
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        return super.generalizes(e) && iz.identifier(e.getInner());
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e) {
        return new Identifier(e);
    }

    /* Constraints Methods */

    public void startsWith(String s) {
        addConstraint(e -> az.identifier(e.inner).getText().startsWith(s));
    }

    public void contains(String s) {
        addConstraint(e -> az.identifier(e.inner).getText().contains(s));
    }

    public void endWith(String s) {
        addConstraint(e -> az.identifier(e.inner).getText().endsWith(s));
    }
}
