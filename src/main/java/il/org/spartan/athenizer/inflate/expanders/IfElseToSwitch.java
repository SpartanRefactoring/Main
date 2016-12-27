package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Issue #1018
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
@SuppressWarnings("unused")
public class IfElseToSwitch extends ReplaceCurrentNode<IfStatement> implements TipperCategory.InVain {
  /** [[SuppressWarningsSpartan]] */
  @Override public ASTNode replacement(final IfStatement ¢) {
    final List<Expression> xs = getAllExpressions(¢);
    if (!isMyCase(xs))
      return null;
    final AST create = ¢.getAST();
    final SwitchStatement $ = create.newSwitchStatement();
    $.setExpression(duplicate.of(az.simpleName(step.left(az.comparison(xs.get(0))))));
    final List<Statement> ss = step.statements($);
    final List<Block> bs = getAllBlocks(¢);
    int i = 0;
    for (final Expression x : xs) {
      final SwitchCase sc = create.newSwitchCase();
      sc.setExpression(duplicate.of(step.right(az.comparison(x))));
      ss.add(sc);
      for (final Statement s : step.statements(bs.get(i)))
        ss.add(duplicate.of(s));
      ss.add(create.newBreakStatement());
      ++i;
    }
    if (xs.size() != bs.size()) {
      // Meaning - there was a finishing else statement
      final SwitchCase sc = create.newSwitchCase();
      sc.setExpression(null);
      ss.add(sc);
      for (final Statement s : step.statements(bs.get(i)))
        ss.add(duplicate.of(s));
      ss.add(create.newBreakStatement());
    }
    return $;
  }

  private static boolean isMyCase(final List<Expression> xs) {
    if (xs == null || xs.isEmpty() || !iz.infixEquals(xs.get(0)))
      return false;
    InfixExpression px = az.comparison(xs.get(0));
    if (!iz.infixEquals(px))
      return false;
    final SimpleName switchVariable = !iz.simpleName(step.left(px)) ? null : az.simpleName(step.left(px));
    if (switchVariable == null)
      return false;
    for (final Expression e : xs) {
      px = az.comparison(e);
      if (!iz.infixEquals(px))
        return false;
      final SimpleName currName = !iz.simpleName(step.left(px)) ? null : az.simpleName(step.left(px));
      if (currName == null || !currName.getIdentifier().equals(switchVariable.getIdentifier()))
        return false;
    }
    return true;
  }

  private static List<Expression> getAllExpressions(final IfStatement s) {
    final List<Expression> $ = new ArrayList<>();
    for (Statement p = s; iz.ifStatement(p); p = az.ifStatement(p).getElseStatement())
      $.add(step.expression(az.ifStatement(p)));
    return $;
  }

  private static List<Block> getAllBlocks(final IfStatement s) {
    final List<Block> $ = new ArrayList<>();
    Statement p;
    for (p = s; iz.ifStatement(p); p = az.ifStatement(p).getElseStatement()) {
      Block b = az.block(duplicate.of(az.ifStatement(p).getThenStatement()));
      if (b == null) {
        b = s.getAST().newBlock();
        step.statements(b).add(az.statement(duplicate.of(az.ifStatement(p).getThenStatement())));
      }
      $.add(b);
    }
    if (p == null)
      return $;
    if (az.block(p) != null) {
      $.add(az.block(p));
      return $;
    }
    final Block b = s.getAST().newBlock();
    step.statements(b).add(duplicate.of(p));
    $.add(b);
    return $;
  }

  // private static List<Const>
  @Override public String description(final IfStatement __) {
    return "Replace if-else statement with one parameter into switch-case";
  }
}