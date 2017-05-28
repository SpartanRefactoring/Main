package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;

import java.util.HashMap;

/**
 * @author michalcohen
 * @since 30-04-2017.
 */
public class PruningTest extends PsiTypeHelper {
    public void testPruneExpression() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "expression(0);"));
        Encapsulator pruned = Pruning.prune(root, new HashMap<>());
        assertEquals(pruned.getChildren().size(), 7);
        assertTrue(iz.generic(pruned.getChildren().get(6).getChildren().get(0).getChildren().get(1).getChildren().get(0)));
        assertFalse(iz.generic(pruned.getChildren().get(3)));
    }

    public void testPruneStatement() throws Exception {
        Encapsulator root = Encapsulator.buildTreeFromPsi(createTestIfStatement("x > 2", "statement(0);"));
        Encapsulator pruned = Pruning.prune(root, new HashMap<>());
        assertEquals(pruned.getChildren().size(), 7);
        assertTrue(iz.generic(pruned.getChildren().get(6).getChildren().get(0).getChildren().get(1)));
        assertFalse(iz.generic(pruned.getChildren().get(3)));
    }
}