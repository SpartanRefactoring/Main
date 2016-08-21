package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.utils.*;

/** convert
 *
 * <pre>
 * if {a) g();}
 * </pre>
 *
 * into
 *
 * <pre>
 * if (a)
 *   g();
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class BlockSingleton extends Wring.ReplaceCurrentNode<Block> implements Kind.SyntacticBaggage {
  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }

  @Override Statement replacement(final Block b) {
    final ASTNode parent = parent(b);
    return !(parent instanceof Statement) || Is.is(parent, ASTNode.TRY_STATEMENT, ASTNode.SYNCHRONIZED_STATEMENT) ? null
        : replacement(Utils.onlyOne(statements(b)));
  }

  private static Statement replacement(final Statement $) {
    return $ == null || Is.blockEssential($) ? null : duplicate($);
  }
}
