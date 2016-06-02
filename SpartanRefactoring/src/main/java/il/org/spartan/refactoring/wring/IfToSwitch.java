package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.BindingUtils;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

/**
 * A wring to replace if statements with switch statements.
 *
 * <pre>
 * <code>
 * if (x == 1)
 *   return 1;
 * else if (x == 2)
 *   return 2;
 * else
 *   return -1;
 * </code>
 * </pre>
 *
 * becomes
 *
 * <pre>
 * <code>
 * switch(x) {
 * case 1:
 *   return 1;
 * case 2:
 *   return 2;
 * default:
 *   return -1;
 * </code>
 * </pre>
 *
 * TODO Ori: add binding if needed
 *
 * @author Ori Roth
 * @since 2016/05/11
 */
@Deprecated public class IfToSwitch extends ReplaceCurrentNode<IfStatement> {
  final static boolean PRIORITY = false;
  final static int MIN_CASES_THRESHOLD = 3;
  final static ASTMatcher m = new ASTMatcher();

  static boolean isSimpleComparison(Expression e) {
    return e instanceof InfixExpression && ((InfixExpression) e).getOperator().equals(Operator.EQUALS)
        && BindingUtils.isSimple(((InfixExpression) e).getRightOperand())
        || e instanceof MethodInvocation && "equals".equals(((MethodInvocation)e).getName().getIdentifier())
            && ((MethodInvocation) e).arguments().size() == 1
            && BindingUtils.isSimple((Expression) ((MethodInvocation) e).arguments().get(0));
  }
  static boolean isSimpleComparison(Expression e, Expression v) {
    return e instanceof InfixExpression && ((InfixExpression) e).getOperator().equals(Operator.EQUALS)
        && getLeftFromComparison(e).subtreeMatch(m, v) && BindingUtils.isSimple(getRightFromComparison(e))
        || e instanceof MethodInvocation && "equals".equals(((MethodInvocation)e).getName().getIdentifier())
            && getLeftFromComparison(e).subtreeMatch(m, v) && ((MethodInvocation) e).arguments().size() == 1
            && BindingUtils.isSimple(getRightFromComparison(e));
  }
  static Expression getLeftFromComparison(Expression e) {
    return e instanceof InfixExpression ? ((InfixExpression) e).getLeftOperand()
        : !(e instanceof MethodInvocation)?null:!(((MethodInvocation)e).getExpression() instanceof StringLiteral)?((MethodInvocation)e).getExpression():(Expression)((MethodInvocation)e).arguments().get(0);
  }
  static Expression getRightFromComparison(Expression e) {
    return e instanceof InfixExpression ? ((InfixExpression) e).getRightOperand()
        : !(e instanceof MethodInvocation) ? null
            : ((MethodInvocation) e).getExpression() instanceof StringLiteral ? ((MethodInvocation) e).getExpression()
                : (Expression) ((MethodInvocation) e).arguments().get(0);
  }
  @SuppressWarnings("unchecked") protected void addStatements(SwitchStatement $, Statement s) {
    final int i = $.statements().size();
    if (!(s instanceof Block))
      $.statements().add(scalpel.duplicate(s));
    else
      scalpel.duplicateInto(((Block) s).statements(), $.statements());
    if (!SwitchBreakReturn.caseEndsWithSequencer($.statements(), i))
      $.statements().add(s.getAST().newBreakStatement());
  }
  @SuppressWarnings("unchecked") protected SwitchStatement buildSwitch(SwitchStatement $, Statement s, Expression v) {
    if (s == null)
      return $;
    if (s instanceof IfStatement) {
      final IfStatement i = (IfStatement) s;
      final Expression e = i.getExpression();
      if (!isSimpleComparison(e, v))
        return null;
      final SwitchCase c = s.getAST().newSwitchCase();
      c.setExpression(scalpel.duplicate(getRightFromComparison(e)));
      $.statements().add(c);
      if (!PRIORITY && i.getThenStatement() instanceof Block && ((Block) i.getThenStatement()).statements().size() == 1)
        return null;
      addStatements($, i.getThenStatement());
      return buildSwitch($, ((IfStatement) s).getElseStatement(), v);
    }
    final SwitchCase c = s.getAST().newSwitchCase();
    c.setExpression(null);
    $.statements().add(c);
    if (!PRIORITY && s instanceof Block && ((Block) s).statements().size() == 1)
      return null;
    addStatements($, s);
    return $;
  }
  @SuppressWarnings("unchecked") protected static int countCases(SwitchStatement s) {
    int $ = 0;
    for (final Statement i : (Iterable<Statement>) s.statements())
      if (i instanceof SwitchCase)
        ++$;
    return $;
  }
  @Override ASTNode replacement(IfStatement n) {
    if (n.getParent() instanceof IfStatement)
      return null;
    Expression e = n.getExpression();
    if (!isSimpleComparison(e))
      return null;
    e = getLeftFromComparison(e);
    if (!BindingUtils.isSimple(e))
      return null;
    SwitchStatement $ = n.getAST().newSwitchStatement();
    $.setExpression(scalpel.duplicate(e));
    $ = buildSwitch($, n, e);
    if ($ == null || countCases($) < MIN_CASES_THRESHOLD)
      return null;
    return $;
  }
  @Override String description(@SuppressWarnings("unused") IfStatement __) {
    return "Replace if with switch";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.SWITCH_IF_CONVERTION;
  }
}
