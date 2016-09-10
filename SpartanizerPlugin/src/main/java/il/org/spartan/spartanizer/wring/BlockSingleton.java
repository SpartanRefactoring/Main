package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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
  private static Statement replacement(final Statement $) {
    return $ == null || iz.blockEssential($) ? null : duplicate.of($);
  }

  @Override String description(@SuppressWarnings("unused") final Block ____) {
    return "Remove redundant curly braces.";
  }

  @Override Statement replacement(final Block b) {
    final ASTNode parent = step.parent(b);
    return !(parent instanceof Statement) || iz.is(parent, ASTNode.TRY__STATEMENT, ASTNode.SYNCHRONIZED__STATEMENT) ? null
        : replacement(lisp.onlyOne(statements(b)));
  }
}
