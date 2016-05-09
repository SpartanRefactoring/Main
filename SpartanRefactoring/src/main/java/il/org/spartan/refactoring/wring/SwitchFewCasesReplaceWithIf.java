package il.org.spartan.refactoring.wring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

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
 * </pre></code> with
 *
 * <pre>
 * <code>
 * if (x == 1) {
 *   System.out.println("1 detected!");
 * } else {
 *   System.out.println("error!");
 * }
 * </pre></code> TODO Ori: add tests
 *
 * @author Ori Roth
 * @since 2016/05/09
 */
@SuppressWarnings("unchecked") public class SwitchFewCasesReplaceWithIf extends ReplaceCurrentNode<SwitchStatement> {
  protected IfStatement buildIfStatement(AST a, List<Statement> iss, Expression e, Expression t) {
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
    if (comments != null)
      comments.duplicateWithCommentsInto(iss, b.statements());
    $.setThenStatement(b);
    return $;
  }
  protected IfStatement buildIfStatement(AST a, List<Statement> iss, List<Statement> ess, Expression e, Expression t) {
    final IfStatement $ = buildIfStatement(a, iss, e, t);
    final Block b = a.newBlock();
    if (comments != null)
      comments.duplicateWithCommentsInto(ess, b.statements());
    $.setElseStatement(b);
    return $;
  }
  @Override ASTNode replacement(SwitchStatement n) {
    if (!n.getAST().hasResolvedBindings())
      return null;
    final List<Statement> sss = n.statements();
    final List<Statement> iss = new ArrayList<>();
    final List<Statement> ess = new ArrayList<>();
    int si = 0;
    Expression t = null;
    for (; si < sss.size(); ++si) {
      final Statement cs = sss.get(si);
      if (cs instanceof BreakStatement)
        break;
      if (cs instanceof SwitchCase)
        if (t == null)
          t = ((SwitchCase) cs).getExpression();
        else
          return null;
      else
        iss.add(cs);
      if (cs instanceof ReturnStatement || cs instanceof ThrowStatement)
        break;
    }
    for (; si < sss.size(); ++si)
      if (sss.get(si) instanceof SwitchCase)
        break;
    if (si == sss.size())
      return buildIfStatement(n.getAST(), iss, n.getExpression(), t);
    if (!((SwitchCase) sss.get(si)).isDefault())
      return null;
    for (++si; si < sss.size(); ++si) {
      final Statement cs = sss.get(si);
      if (cs instanceof SwitchCase)
        return null;
      if (!(cs instanceof BreakStatement))
        ess.add(cs);
    }
    if (comments != null) {
      comments.remove(comments.get(iss));
      comments.remove(comments.get(ess));
    }
    return buildIfStatement(n.getAST(), iss, ess, n.getExpression(), t);
  }
  @Override String description(@SuppressWarnings("unused") SwitchStatement __) {
    return "Replace short switch statement with an if statement";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.SWITCH_IF_CONVERTION;
  }
}
