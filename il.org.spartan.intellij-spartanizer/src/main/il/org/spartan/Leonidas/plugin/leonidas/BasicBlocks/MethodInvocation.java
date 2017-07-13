package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * Basic Block for Leonidas Tippers.
 * i.g.: list.isEmpty() => identifier0.methodInvoation1();
 * @author Oren Afek
 * @since 30/6/17
 */
public class MethodInvocation extends Identifiable {

    private static final String TEMPLATE = "methodInvocation";

    // DO NOT REMOVE, REFLECTION PURPOSES!
    public MethodInvocation() {
        super(TEMPLATE);
    }

    public MethodInvocation(Encapsulator e) {
        super(e, TEMPLATE);
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new MethodInvocation(e);
    }

    @Override
    public void copyTo(GenericEncapsulator dst) {
        super.copyTo(dst);
    }

    @Override
    public boolean conforms(PsiElement e) {
        return iz.identifier(e) && super.conforms(e);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
