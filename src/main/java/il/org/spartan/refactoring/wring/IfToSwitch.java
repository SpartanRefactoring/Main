package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/**
 * A wring to replace if statements with switch statements.
 *
 * <pre> <code> if (x == 1) return 1; else if (x == 2) return 2; else return -1;
 * </code> </pre>
 *
 * becomes
 *
 * <pre> <code> switch(x) { case 1: return 1; case 2: return 2; default: return
 * -1; </code> </pre>
 *
 * TODO Ori: add binding if needed
 *
 * @author Ori Roth
 * @since 2016/05/11
 */
@Deprecated public class IfToSwitch extends ReplaceCurrentNode<IfStatement> implements Kind.SWITCH_IF_CONVERTION {
  final static boolean PRIORITY = false;
  final static int MIN_CASES_THRESHOLD = 3;
  final static ASTMatcher m = new ASTMatcher();

  static boolean isSimpleComparison(final Expression e) {
    return e instanceof InfixExpression && ((InfixExpression) e).getOperator().equals(Operator.EQUALS) && BindingUtils.isSimple(((InfixExpression) e).getRightOperand())
        || e instanceof MethodInvocation && "equals".equals(((MethodInvocation) e).getName().getIdentifier()) && ((MethodInvocation) e).arguments().size() == 1
        && BindingUtils.isSimple((Expression) ((MethodInvocation) e).arguments().get(0));
  }
  static boolean isSimpleComparison(final Expression e, final Expression v) {
    return e instanceof InfixExpression && ((InfixExpression) e).getOperator().equals(Operator.EQUALS) && getLeftFromComparison(e).subtreeMatch(m, v)
        && BindingUtils.isSimple(getRightFromComparison(e)) || e instanceof MethodInvocation && "equals".equals(((MethodInvocation) e).getName().getIdentifier())
        && getLeftFromComparison(e).subtreeMatch(m, v) && ((MethodInvocation) e).arguments().size() == 1 && BindingUtils.isSimple(getRightFromComparison(e));
  }
  static Expression getLeftFromComparison(final Expression e) {
    return e instanceof InfixExpression ? ((InfixExpression) e).getLeftOperand() : !(e instanceof MethodInvocation) ? null
        : !(((MethodInvocation) e).getExpression() instanceof StringLiteral) ? ((MethodInvocation) e).getExpression() : (Expression) ((MethodInvocation) e).arguments().get(0);
  }
  static Expression getRightFromComparison(final Expression e) {
    return e instanceof InfixExpression ? ((InfixExpression) e).getRightOperand() : !(e instanceof MethodInvocation) ? null
        : ((MethodInvocation) e).getExpression() instanceof StringLiteral ? ((MethodInvocation) e).getExpression() : (Expression) ((MethodInvocation) e).arguments().get(0);
  }
  @SuppressWarnings("unchecked") protected void addStatements(final SwitchStatement $, final Statement s) {
    final int i = $.statements().size();
    if (!(s instanceof Block))
      $.statements().add(scalpel.duplicate(s));
    else
      scalpel.duplicateInto(((Block) s).statements(), $.statements());
    if (!SwitchBreakReturn.caseEndsWithSequencer($.statements(), i))
      $.statements().add(s.getAST().newBreakStatement());
  }
  @SuppressWarnings("unchecked") protected @Nullable SwitchStatement buildSwitch(final SwitchStatement $, final Statement s, final Expression v) {
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
  @SuppressWarnings("unchecked") protected static int countCases(final SwitchStatement s) {
    int $ = 0;
    for (final Statement i : (Iterable<Statement>) s.statements())
      if (i instanceof SwitchCase)
        ++$;
    return $;
  }
  @Override ASTNode replacement(final IfStatement s) {
    if (s.getParent() instanceof IfStatement)
      return null;
    Expression e = s.getExpression();
    if (!isSimpleComparison(e))
      return null;
    e = getLeftFromComparison(e);
    if (!BindingUtils.isSimple(e))
      return null;
    SwitchStatement $ = s.getAST().newSwitchStatement();
    $.setExpression(scalpel.duplicate(e));
    $ = buildSwitch($, s, e);
    return $ != null && countCases($) >= MIN_CASES_THRESHOLD ? $ : null;
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace if with switch";
  }
}
