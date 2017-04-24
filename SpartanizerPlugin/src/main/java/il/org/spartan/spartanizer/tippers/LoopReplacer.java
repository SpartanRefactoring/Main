package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import nano.ly.*;

/** An abstract class to manipulate bodies of loops.
 * @author Yossi Gil
 * @since 2017-03-05 */
public abstract class LoopReplacer<S extends Statement> extends ReplaceCurrentNode<S> {
  private static final long serialVersionUID = 0x8FF10353929C0E6L;
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
    lastStatement = the.lastOf(statements);
    updater = az.expressionStatement(lastStatement).getExpression();
    updates = compute.updateSpots(updater);
  }

  protected final boolean validUpdater() {
    return statements != null && statements.size() > 1 && validUpdates() && noContinue();
  }

  boolean noContinue() {
    return !haz.continueStatement(body);
  }

  boolean validUpdates() {
    return updates.stream().allMatch(SimpleName.class::isInstance);
  }

  public boolean bodyDeclaresElementsOf(final ASTNode n) {
    return extract.fragments(block).stream().noneMatch(λ -> collect.usesOf(λ.getName()).in(n).isEmpty());
  }
}