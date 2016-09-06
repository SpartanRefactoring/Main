package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

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

  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }

  @Override Statement replacement(final Block b) {
    final ASTNode parent = step.parent(b);
    return !(parent instanceof Statement) || iz.is(parent, ASTNode.TRY_STATEMENT, ASTNode.SYNCHRONIZED_STATEMENT) ? null
        : replacement(lisp.onlyOne(statements(b)));
  }
}
