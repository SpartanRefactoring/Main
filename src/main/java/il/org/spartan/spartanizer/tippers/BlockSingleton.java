package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class BlockSingleton extends ReplaceCurrentNode<Block>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x9FD71EDA4C3056AL;

  private static Statement replacement(final Statement $) {
    return $ == null || iz.blockEssential($) || iz.isVariableDeclarationStatement($) ? null : copy.of($);
  }

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }

  @Override public Statement replacement(final Block ¢) {
    final ASTNode $ = parent(¢);
    return !($ instanceof Statement) || iz.nodeTypeIn($, ASTNode.TRY_STATEMENT, ASTNode.SYNCHRONIZED_STATEMENT) ? null
        : replacement(onlyOne(statements(¢)));
  }
}
