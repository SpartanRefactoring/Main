package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>if (a) (g();}</code> into
 * <code>if (a) g();</code>
 *
 * @author Yossi Gil
 * @since 2015-09-09
 */
public class BlockSingleton extends Wring.ReplaceCurrentNode<Block> implements Kind.REMOVE_REDUNDANT_PUNCTUATION {
  @Override Statement replacement(final Block b) {
    final List<Statement> ss = expose.statements(b);
    final ASTNode parent = parent(b);
    if (!(parent instanceof Statement) || parent instanceof TryStatement || parent instanceof SynchronizedStatement
        || ss.size() == 1 && ss.get(0) instanceof VariableDeclarationStatement || ss.size() != 1)
      return null;
    final Statement $ = ss.get(0);
    return Is.blockEssential($) ? null : scalpel.duplicate($);
  }
  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }
}