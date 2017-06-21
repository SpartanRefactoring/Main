package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * @author michalcohen
 * @since 20-06-2017
 */
public class AnyNumberOfInterfaces extends AnyNumberOfBasedNames {

    private static final String TEMPLATE = "interface";

    public AnyNumberOfInterfaces(){
        super(TEMPLATE);
    }

    public AnyNumberOfInterfaces(Encapsulator e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    @Override
    protected String getName(PsiElement e) {
        return az.javaCodeReference(e).getQualifiedName();// not sure
    }

    @Override
    public boolean conforms(PsiElement e) {
        return iz.javaCodeReference(e) && super.conforms(e);
    }

    @Override
    public int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m) {
        if (i.value().getParent() == null) return 1;
        Wrapper<Integer> count = new Wrapper<>(0);
        //noinspection StatementWithEmptyBody
        i.value().getParent().accept(n -> {
            if (generalizes(n, m).matches()) count.set(count.get() + 1);
        });
        return count.get();
    }

    @Override
    public QuantifierBasedNames create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        Encapsulator e2 = internalEncapsulator(e);
        return new AnyNumberOfInterfaces(e, e2);
    }

    private Encapsulator internalEncapsulator(Encapsulator e) {
        return new BaseType(e);
    }
}
