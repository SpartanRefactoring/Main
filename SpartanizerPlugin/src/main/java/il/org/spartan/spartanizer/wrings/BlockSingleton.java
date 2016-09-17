package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
 *
 * <pre>
 * if a) g();}
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
public final class BlockSingleton extends ReplaceCurrentNode<Block> implements Kind.SyntacticBaggage {
  private static Statement replacement(final Statement $) {
    return $ == null || iz.blockEssential($) ? null : duplicate.of($);
  }

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }

  @Override public Statement replacement(final Block b) {
    final ASTNode parent = step.parent(b);
    return !(parent instanceof Statement) || iz.is(parent, ASTNode.TRY_STATEMENT, ASTNode.SYNCHRONIZED_STATEMENT) ? null
        : replacement(onlyOne(statements(b)));
  }
}
