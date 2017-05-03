package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiMethodCallExpression;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;

import java.util.Optional;

import static il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters.ID;

/**
 * This class helps generate generic trees representing code template written
 * in the Leonidas Language.
 *
 * @author michalcohen
 * @since 07-01-2017
 */
public class Pruning {

    /**
     * Prunes all the stubs of the form "stub();" where "stub()"
     * is a method call for the method defined in GenericPsiElementStub.
     *
     * @param n - the root from which all such stubs are pruned
     */
    public static Encapsulator prune(Encapsulator n) {
        assert (n != null);
        n.accept(e1 -> {
            if (!iz.methodCallExpression(e1.getInner()))
				return;
            PsiMethodCallExpression exp = az.methodCallExpression(e1.getInner());

            Optional.ofNullable(GenericPsiElementStub.StubName.valueOfMethodCall(exp)).ifPresent(y -> {
                Encapsulator prev = Pruning.getRealParent(e1, y);
                GenericEncapsulator x = y.getGenericElement(prev.getInner(), exp.getUserData(ID));
                prev.generalize(x);
            });
        });
        return n;
    }

    /**
     * @param n method call representing generic element.
     * @param y the type of the generic method call.
     * @return the highest generic parent.
     */
    public static Encapsulator getRealParent(Encapsulator n, GenericPsiElementStub.StubName y) {
        Encapsulator prev = n, next = n.getParent();
        for (; y.goUpwards(prev, next); next = next.getParent())
			prev = next;
        return prev;
    }
}
