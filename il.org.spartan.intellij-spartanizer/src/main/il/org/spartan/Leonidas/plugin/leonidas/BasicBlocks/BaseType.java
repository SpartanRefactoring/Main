package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * A basic block representing a general declaration of implementation of an interface.
 * For example: class Class0 implements Interface1 {...}
 * @author michalcohen
 * @since 20-06-2017.
 */
public class BaseType extends NamedElement {

    private static final String TEMPLATE = "Interface";
    public BaseType(Encapsulator e) {
        super(e, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    public BaseType() {
        super(TEMPLATE);
    }
    @Override
    protected String getName(PsiElement ¢) {
        return az.javaCodeReference(¢).getQualifiedName();
    }

    @Override
    public boolean conforms(PsiElement ¢) {
        return iz.javaCodeReference(¢) && super.conforms(¢);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new BaseType(e);
    }
}
