package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** An abstract class to manipulate bodies of loops.
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-03-05 */
public abstract class LoopReplacer<S extends Statement> extends ReplaceCurrentNode<S> {
  private static final long serialVersionUID = 648254692142989542L;
  protected Statement body;
  protected Block block;
  protected List<Statement> statements;
  protected Statement lastStatement;
  protected Expression updater;
  protected List<ASTNode> updates;

  public void fillUp(final Statement loopBody) {
    body = loopBody;
    block = az.block(body);
    statements = statements(block);
    lastStatement = last(statements);
    updater = az.expressionStatement(lastStatement).getExpression();
    updates = extract.updatedVariables(updater);
  }

  protected final boolean validUpdater() {
    return statements != null && statements.size() > 1 && validUpdates() && noContinue();
  }

  boolean noContinue() {
    return !haz.ContinueStatement(body);
  }

  boolean validUpdates() {
    return updates.stream().allMatch(SimpleName.class::isInstance);
  }

  public boolean bodyDeclaresElementsOf(final ASTNode n) {
    return extract.fragments(block).stream().allMatch(λ -> !collect.usesOf(λ.getName()).in(n).isEmpty());
  }
}