package il.org.spartan.spartanizer.tippers;

import il.org.spartan.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
* switch (x) {
* case a: (commands)
*   break;
* default: (other commands)
* }
 * </pre>
 *
 * into
 *
 * <pre>
* if(x == a) {
*   (commands)
* }
* else {
*   (other commands)
* }
 * </pre>
 *
 * . Tested in {@link Issue0916}
 * @author Yuval Simon
 * @since 2016-12-18 */
// TODO Yuval Simon: remove @SuppressWarnings({ "unchecked" }) and use class
// step --yg
public class SwitchWithOneCaseToIf extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  // @Override @SuppressWarnings({ "unchecked" }) public ASTNode
  // replacement(final SwitchStatement s) {
  // if (s == null)
  // return null;
  // final List<SwitchCase> $ = extract.switchCases(s);
  // // TODO: Yuval Simon - I do not think you need this call
  // statements(s);
  // final AST a = s.getAST();
  // // TODO: Yuval Simon -rename to $ --yg
  // final Block res = a.newBlock();
  // final IfStatement r = a.newIfStatement();
  // // TODO: Yuval Simon - use step.pair --yg
  // res.statements().add(r);
  // final Block b1 = a.newBlock();
  // final Block b2 = a.newBlock();
  // r.setThenStatement(b1);
  // r.setElseStatement(b2);
  // r.setExpression(makeFrom(s, $, a));
  // addStatements(firstComOfCase(s), s, b1);
  // addStatements(firstComOfDefault(s), s, b2);
  // return res;
  // }
  @Override @SuppressWarnings("unused") public String description(final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final List<switchBranch> l = switchBranch.intoBranches(s);
    if (l.size() != 2)
      return null;
    final switchBranch b1 = lisp.first(l);
    final switchBranch b2 = lisp.last(l);
    if (!b1.hasDefault() && !b2.hasDefault() || b1.hasFallThrough() || b2.hasFallThrough() || !b1.hasStatements() || !b2.hasStatements()
        || haz.sideEffects(step.expression(s)) && (b1.hasDefault() ? b2 : b1).cases().size() > 1)
      return null;
    final AST a = s.getAST();
    final Block $ = a.newBlock();
    final IfStatement r = a.newIfStatement();
    step.statements($).add(r);
    switchBranch t = lisp.first(l).hasDefault() ? lisp.last(l) : lisp.first(l);
    r.setExpression(makeFrom(s, t.cases(), a));
    Block b = a.newBlock();
    r.setThenStatement(b);
    step.statements(b).addAll(switchBranch.removeBreakSequencer(t.statements()));
    b = a.newBlock();
    r.setElseStatement(b);
    t = lisp.first(l).hasDefault() ? lisp.first(l) : lisp.last(l);
    step.statements(b).addAll(switchBranch.removeBreakSequencer(t.statements()));
    return $;
  }

  private static InfixExpression makeFrom(final SwitchStatement s, final List<SwitchCase> cs, final AST t) {
    InfixExpression $ = null;
    for (final SwitchCase c : cs) {
      if (c.isDefault())
        continue;
      // TODO: Yuval Simon please use fluent API with class subject --yg
      final InfixExpression n = t.newInfixExpression();
      // subject.pair(copy.of(expression(s), copy.of(expression(c))
      n.setOperator(InfixExpression.Operator.EQUALS);
      n.setLeftOperand(copy.of(expression(s)));
      n.setRightOperand(copy.of(expression(c)));
      if ($ == null)
        $ = n;
      else {
        // TODO: Yuval Simon please use fluent API with class subject --yg
        final InfixExpression nn = t.newInfixExpression();
        nn.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
        nn.setLeftOperand($);
        nn.setRightOperand(n);
        $ = nn;
      }
    }
    return $;
  }
}
