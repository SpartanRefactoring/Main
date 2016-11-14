package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * if (a) g();}
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
public final class BlockSingleton extends ReplaceCurrentNode<Block> implements TipperCategory.SyntacticBaggage {
  private static Statement replacement(final Statement $) {
    return $ == null || iz.blockEssential($) || iz.isVariableDeclarationStatement($) ? null : duplicate.of($);
  }

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }

  @Override public Statement replacement(final Block b) {
    final ASTNode parent = parent(b);
    return !(parent instanceof Statement) || iz.nodeTypeIn(parent, ASTNode.TRY_STATEMENT, ASTNode.SYNCHRONIZED_STATEMENT) ? null
        : replacement(onlyOne(statements(b)));
  }
}
