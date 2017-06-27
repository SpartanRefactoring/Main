package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * Represents a list of any number of interfaces a class declares it implements
 * class Class0 implements AnyNumberOfInterfaces1 {...}
 * @author michalcohen
 * @since 20-06-2017
 */
public class AnyNumberOfInterfaces extends AnyNumberOfBasedNames {

    private static final String TEMPLATE = "AnyNumberOfInterfaces";

    public AnyNumberOfInterfaces(){
        super(TEMPLATE);
    }

    public AnyNumberOfInterfaces(Encapsulator e, Encapsulator i) {
        super(e, TEMPLATE, i);
    }

    @Override
    protected String getName(PsiElement ¢) {
        return az.javaCodeReference(¢).getQualifiedName();// not sure
    }

    @Override
    public boolean conforms(PsiElement ¢) {
        return iz.javaCodeReference(¢) && super.conforms(¢);
    }

    @Override
    public int getNumberOfOccurrences(EncapsulatorIterator i, Map<Integer, List<PsiElement>> m) {
        if (i.value().getParent() == null) return 1;
        Wrapper<Integer> count = new Wrapper<>(0);
        //noinspection StatementWithEmptyBody
        i.value().getParent().accept(λ -> {
            if (generalizes(λ, m).matches()) count.set(count.get() + 1);
        });
        return count.get();
    }

    @Override
    public QuantifierBasedNames create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new AnyNumberOfInterfaces(e, internalEncapsulator(e));
    }

    private Encapsulator internalEncapsulator(Encapsulator ¢) {
        return new BaseType(¢);
    }
}
