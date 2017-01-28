package il.org.spartan.spartanizer.tippers;

import java.util.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
* switch (x) {
*   case a:
*      (commands)
*      break;
*   default:
*      (other commands)
* }
 * </pre>
 *
 * into
 *
 * <pre>
* if(x == a) {
*   (commands)
* } else {
*   (other commands)
* }
 * </pre>
 *
 * . Tested in {@link Issue0916}
 * @author Yuval Simon
 * @since 2016-12-18 */
public class SwitchWithOneCaseToIf extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Unite {
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }

  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final List<switchBranch> l = switchBranch.intoBranches(s);
    if (l.size() != 2)
      return null;
    switchBranch t = lisp.first(l).hasDefault() ? lisp.last(l) : lisp.first(l);
    if (iz.stringLiteral(expression(lisp.first(t.cases()))))
      return null;
    final switchBranch s1 = lisp.first(l), s2 = lisp.last(l);
    if (!s1.hasDefault() && !s2.hasDefault() || s1.hasFallThrough() || s2.hasFallThrough() || !s1.hasStatements() || !s2.hasStatements()
        || haz.sideEffects(step.expression(s)) && (s1.hasDefault() ? s2 : s1).cases().size() > 1)
      return null;
    final AST a = s.getAST();
    final Block b1 = a.newBlock(), b2 = a.newBlock();
    t = lisp.first(l).hasDefault() ? lisp.first(l) : lisp.last(l);
    step.statements(b2).addAll(switchBranch.removeBreakSequencer(t.statements()));
    t = lisp.first(l).hasDefault() ? lisp.last(l) : lisp.first(l);
    step.statements(b1).addAll(switchBranch.removeBreakSequencer(t.statements()));
    final Block $ = a.newBlock();
    step.statements($).add(subject.pair(b1, b2).toIf(makeFrom(s, t.cases())));
    return $;
  }

  private static InfixExpression makeFrom(final SwitchStatement s, final List<SwitchCase> cs) {
    InfixExpression $ = null;
    for (final SwitchCase c : cs) {
      if (c.isDefault())
        continue;
      final InfixExpression n = subject.pair(copy.of(expression(s)), copy.of(expression(c))).to(Operator.EQUALS);
      $ = $ == null ? n : subject.pair($, n).to(Operator.CONDITIONAL_OR);
    }
    return $;
  }
}
