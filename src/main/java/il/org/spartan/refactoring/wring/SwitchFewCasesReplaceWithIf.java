package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/**
 * Used to replace a switch statement containing a single case (+ optional
 * default) with an if else statement. replaces
 *
 * <pre>
 * <code>
 * switch (x) {
 * case 1:
 *   System.out.println("1 detected!");
 *   break;
 * default:
 *   System.out.println("error!");
 *   break;
 * }
 * </pre>
 *
 * </code> with
 *
 * <pre>
 * <code>
 * if (x == 1) {
 *   System.out.println("1 detected!");
 * } else {
 *   System.out.println("error!");
 * }
 * </pre>
 *
 * </code> TODO Ori: consider adding option for switch-case-nobreak-default to
 * become if statement + default statements
 *
 * @author Ori Roth
 * @since 2016/05/09
 */
@SuppressWarnings("unchecked") public class SwitchFewCasesReplaceWithIf extends ReplaceCurrentNode<SwitchStatement> implements
    Kind.SWITCH_IF_CONVERTION {
  protected IfStatement buildIfStatement(final AST a, final List<Statement> ss, final Expression e, final Expression t) {
    final IfStatement $ = a.newIfStatement();
    final Expression de = Funcs.duplicate(e), dt = Funcs.duplicate(t);
    if (e.resolveTypeBinding().isPrimitive()) {
      final InfixExpression ie = a.newInfixExpression();
      ie.setOperator(Operator.EQUALS);
      ie.setLeftOperand(de);
      ie.setRightOperand(dt);
      $.setExpression(ie);
    } else {
      final MethodInvocation m = a.newMethodInvocation();
      m.setExpression(de);
      m.setName(a.newSimpleName("equals"));
      m.arguments().add(dt);
      $.setExpression(m);
    }
    final Block b = a.newBlock();
    scalpel.duplicateInto(ss, b.statements());
    $.setThenStatement(b);
    return $;
  }
  protected IfStatement buildIfStatement(final AST a, final List<Statement> ss, final List<Statement> ess, final Expression e,
      final Expression t) {
    final IfStatement $ = buildIfStatement(a, ss, e, t);
    final Block b = a.newBlock();
    scalpel.duplicateInto(ess, b.statements());
    $.setElseStatement(b);
    return $;
  }
  @Override ASTNode replacement(final SwitchStatement s) {
    if (!s.getAST().hasResolvedBindings())
      return null;
    final List<Statement> sss = s.statements();
    final List<Statement> iss = new ArrayList<>();
    final List<Statement> ess = new ArrayList<>();
    int si = 0;
    Expression t = null;
    for (; si < sss.size(); ++si) {
      final Statement cs = sss.get(si);
      if (cs instanceof BreakStatement)
        break;
      if (!(cs instanceof SwitchCase))
        iss.add(cs);
      else {
        if (t != null || ((SwitchCase) cs).isDefault())
          return null;
        t = ((SwitchCase) cs).getExpression();
      }
      if (cs instanceof ReturnStatement || cs instanceof ThrowStatement)
        break;
    }
    if (t == null)
      return null;
    for (; si < sss.size(); ++si)
      if (sss.get(si) instanceof SwitchCase)
        break;
    if (si == sss.size())
      return buildIfStatement(s.getAST(), iss, s.getExpression(), t);
    if (!((SwitchCase) sss.get(si)).isDefault())
      return null;
    for (++si; si < sss.size(); ++si) {
      final Statement cs = sss.get(si);
      if (cs instanceof SwitchCase)
        return null;
      if (!(cs instanceof BreakStatement))
        ess.add(cs);
    }
    return buildIfStatement(s.getAST(), iss, ess, s.getExpression(), t);
  }
  @Override String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Replace short switch statement with an if statement";
  }
}
