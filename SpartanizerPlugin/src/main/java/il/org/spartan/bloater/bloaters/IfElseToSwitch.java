package il.org.spartan.bloater.bloaters;

import static il.org.spartan.lisp.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** {@link Issue #1018}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
@SuppressWarnings("unused")
public class IfElseToSwitch extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 7117505785157896738L;

  @Override public ASTNode replacement(final IfStatement ¢) {
    final List<Expression> xs = getAllExpressions(¢);
    if (!isMyCase(xs))
      return null;
    final AST create = ¢.getAST();
    final SwitchStatement $ = create.newSwitchStatement();
    $.setExpression(copy.of(az.simpleName(left(az.comparison(first(xs))))));
    final List<Statement> ss = statements($);
    final List<Block> bs = getAllBlocks(¢);
    int i = 0;
    for (final Expression x : xs) {
      final SwitchCase sc = create.newSwitchCase();
      sc.setExpression(copy.of(right(az.comparison(x))));
      ss.add(sc);
      statements(bs.get(i)).forEach(λ -> ss.add(copy.of(λ)));
      ss.add(create.newBreakStatement());
      ++i;
    }
    if (xs.size() != bs.size()) {
      // Meaning - there was a finishing else statement
      final SwitchCase sc = create.newSwitchCase();
      sc.setExpression(null);
      ss.add(sc);
      statements(bs.get(i)).forEach(λ -> ss.add(copy.of(λ)));
      ss.add(create.newBreakStatement());
    }
    return $;
  }

  private static boolean isMyCase(final List<Expression> xs) {
    if (xs == null || xs.isEmpty() || !iz.infixEquals(first(xs)))
      return false;
    InfixExpression px = az.comparison(first(xs));
    if (!iz.infixEquals(px))
      return false;
    final SimpleName switchVariable = !iz.simpleName(left(px)) ? null : az.simpleName(left(px));
    if (switchVariable == null)
      return false;
    for (final Expression e : xs) {
      px = az.comparison(e);
      if (!iz.infixEquals(px))
        return false;
      final SimpleName currName = !iz.simpleName(left(px)) ? null : az.simpleName(left(px));
      if (currName == null || !currName.getIdentifier().equals(switchVariable.getIdentifier()))
        return false;
    }
    return true;
  }

  private static List<Expression> getAllExpressions(final IfStatement s) {
    final List<Expression> $ = new ArrayList<>();
    for (Statement p = s; iz.ifStatement(p); p = az.ifStatement(p).getElseStatement()) // TOUGH
      $.add(expression(az.ifStatement(p)));
    return $;
  }

  private static List<Block> getAllBlocks(final IfStatement s) {
    final List<Block> $ = new ArrayList<>();
    final Statement p = addAllBlocks(s, $);
    if (p == null)
      return $;
    if (az.block(p) != null) {
      $.add(az.block(p));
      return $;
    }
    // TODO: Doron, please use class subject
    final Block b = s.getAST().newBlock();
    statements(b).add(copy.of(p));
    $.add(b);
    return $;
  }

  private static Statement addAllBlocks(final IfStatement s, final Collection<Block> collectInto) {
    Statement $ = s;
    for (; iz.ifStatement($); $ = az.ifStatement($).getElseStatement()) {
      final Statement then = copy.of(then(az.ifStatement($)));
      Block b = az.block(then);
      if (b == null) {
        b = s.getAST().newBlock();
        statements(b).add(az.statement(then));
      }
      collectInto.add(b);
    }
    return $;
  }

  @Override public String description(final IfStatement __) {
    return "Replace if-else statement with one parameter into switch-case";
  }
}