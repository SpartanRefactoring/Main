package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.athenizer.zoom.zoomers.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** {@link Issue1018}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-26 */
@SuppressWarnings("unused")
public class IfElseToSwitch extends ReplaceCurrentNode<IfStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x62C675CD2B1FCA22L;

  @Override public ASTNode replacement(final IfStatement ¢) {
    final List<Expression> xs = getAllExpressions(¢);
    if (!isMyCase(xs))
      return null;
    final AST create = ¢.getAST();
    final SwitchStatement ret = create.newSwitchStatement();
    ret.setExpression(copy.of(az.simpleName(left(az.comparison(the.firstOf(xs))))));
    final List<Statement> ss = statements(ret);
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
    if (xs.size() == bs.size())
      return ret;
    final SwitchCase sc = create.newSwitchCase();
    sc.setExpression(null);
    ss.add(sc);
    statements(bs.get(i)).forEach(λ -> ss.add(copy.of(λ)));
    ss.add(create.newBreakStatement());
    return ret;
  }
  private static boolean isMyCase(final List<Expression> xs) {
    if (xs == null || xs.isEmpty() || !iz.infixEquals(the.firstOf(xs)))
      return false;
    InfixExpression px = az.comparison(the.firstOf(xs));
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
    final List<Expression> ret = an.empty.list();
    for (Statement p = s; iz.ifStatement(p); p = az.ifStatement(p).getElseStatement()) // TOUGH
      ret.add(expression(az.ifStatement(p)));
    return ret;
  }
  private static List<Block> getAllBlocks(final IfStatement s) {
    final List<Block> ret = an.empty.list();
    final Statement p = addAllBlocks(s, ret);
    if (p == null)
      return ret;
    if (iz.block(p))
      ret.add(az.block(p));
    else {
      final Block b = s.getAST().newBlock();
      statements(b).add(copy.of(p));
      ret.add(b);
    }
    return ret;
  }
  private static Statement addAllBlocks(final IfStatement s, final Collection<Block> collectInto) {
    Statement ret = s;
    for (; iz.ifStatement(ret); ret = az.ifStatement(ret).getElseStatement()) {
      final Statement then = copy.of(then(az.ifStatement(ret)));
      Block b = az.block(then);
      if (b == null) {
        b = s.getAST().newBlock();
        statements(b).add(az.statement(then));
      }
      collectInto.add(b);
    }
    return ret;
  }
  @Override public String description(final IfStatement __) {
    return "Replace if-else statement with one parameter into switch-case";
  }
}