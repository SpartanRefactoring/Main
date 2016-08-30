package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.subject.*;

/** convert
 *
 * <pre>
 * a = a + 5;
 * </pre>
 *
 * to
 *
 * <post>
 * a += 5;
 * </post>
 *
 * @author Alex
 * @since 2016 */
public final class AssignmentMulExpressionSelf extends Wring.ReplaceCurrentNode<Assignment> implements Kind.Abbreviation {
  @Override String description(final Assignment a) {
    return "Abbreviate " + a + " as x = x*y to x *= y";
  }
  
  @Override ASTNode replacement(Assignment $) {
    if (!iz.isOpAssign(a) || !areAllOperatorsTIMES(az.infixExpression(a.getRightHandSide()))) return null;
    return replace($);
  }
  
  static Assignment replace(Assignment $) {
    $.setOperator(Assignment.Operator.TIMES_ASSIGN);
    $.setRightHandSide(rebuildInfix(az.infixExpression($.getRightHandSide()), $.getLeftHandSide()));
    return $;
  }
  
  static InfixExpression rebuildInfix(InfixExpression e, Expression left) {
    List<Expression> es = extract.allOperands(e);
    for (final Expression ¢ : es)
      if (¢.equals(left)){
        es.remove(¢);
        break;
      }
    return subject.operands(es).to(Operator.TIMES);
  }
  
  static boolean areAllOperatorsTIMES(InfixExpression e) {
    List<InfixExpression.Operator> l = extract.allOperators(e);
    for (final InfixExpression.Operator ¢ : l)
      if (¢ != Operator.TIMES)
        return false;
    return true;
  }
  
/*
  @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(step.left(a), core(s.getExpression())))
      return null;
    r.remove(parent, g);
    r.replace(s, subject.operand(a).toReturn(), g);
    return r;
  }
  */
}
