package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * @author amirsagiv83, michalcohen
 * @since 29-05-2017.
 */
public class Type extends NamedElement{

    private static final String TEMPLATE = "Type";

    public Type(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public Type() {
        super(TEMPLATE);
    }

    @Override
    protected String getName(PsiElement e) {
        return iz.type(e) ? az.type(e).getText() : null;
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return iz.type(next.getInner());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Type(e);
    }
}

