package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;

import java.util.List;
import java.util.Map;

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
    public static Encapsulator prune(Encapsulator n, Map<Integer, List<Matcher.Constraint>> map) {
        assert (n != null);
        final Wrapper<Encapsulator> o = new Wrapper<>();
        n.accept(e1 -> Toolbox.getInstance().getGenericsBasicBlocks().stream()
                .filter(ge -> ge.conforms(e1.getInner()))
                .findFirst()
                .ifPresent(g -> o.set(g.prune(e1, map))));
        if (Toolbox.getInstance().getGenericsBasicBlocks().stream()
                .anyMatch(ge -> ge.conforms(n.getInner())))
            return o.get();
        return n;
    }
}
