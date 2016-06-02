package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

import java.util.Vector;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * A {@link Wring} to replace String appending using StringBuilder or
 * StringBuffer with appending using operator "+"
 * <code><pre>String s = new StringBuilder(myName).append("'s grade is ").append(100).toString();</pre></code>
 * can be replaced with
 * <code><pre>String s = myName + "'s grade is " + 100;</pre></code>
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-11
 */
public class StringFromStringBuilder extends Wring.ReplaceCurrentNode<MethodInvocation> {
  // list of class extending Expression class, that need to be surrounded by
  // parenthesis
  // when put out of method arguments list
  private final Class<?>[] np = { InfixExpression.class };

  /**
   * Checks if an expression need parenthesis in order to interpreted correctly
   *
   * @param e an Expression
   * @return whether or not this expression need parenthesis when put together
   *         with other expressions in infix expression. There could be non
   *         explicit parenthesis if the expression is located in an arguments
   *         list, so making it a part of infix expression require additional
   *         parenthesis
   */
  private boolean isParethesisNeeded(Expression e) {
    for (final Class<?> c : np)
      if (c.isInstance(e))
        return true;
    return false;
  }
  /**
   * Adds parenthesis to expression if needed.
   *
   * @param e an Expression
   * @return e itself if no parenthesis needed, otherwise a
   *         ParenthesisExpression containing e
   */
  private Expression addParenthesisIfNeeded(Expression e) {
    final AST a = e.getAST();
    if (!isParethesisNeeded(e))
      return e;
    final ParenthesizedExpression $ = a.newParenthesizedExpression();
    $.setExpression((Expression) ASTNode.copySubtree(a, e));
    return $;
  }
  /**
   * @param e an Expression
   * @return true iff e is a String
   */
  private boolean isString(Expression e) {
    return e instanceof StringLiteral;
  }
  @Override ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(i.getName().toString()))
      return null;
    final Vector<Expression> sll = new Vector<>();
    MethodInvocation r = i;
    boolean hs = false;
    // collecting strings from append method arguments list and from class
    // instance creation arguments list
    while (true) {
      final Expression e = r.getExpression();
      if (e instanceof ClassInstanceCreation) {
        final String t = ((ClassInstanceCreation) e).getType().toString();
        if (!"StringBuffer".equals(t) && !"StringBuilder".equals(t))
          return null;
        if (!((ClassInstanceCreation) e).arguments().isEmpty() && "StringBuilder".equals(t)) {
          final Expression a = (Expression) ((ClassInstanceCreation) e).arguments().get(0);
          sll.insertElementAt(addParenthesisIfNeeded(a), 0);
          hs = isString(a) || hs;
        }
        if (!hs) {
          // creating a "" string literal to ensure final value interpreted as
          // a string.
          // this expression is created twice, maybe unnecessarily
          final StringLiteral es = i.getAST().newStringLiteral();
          es.setLiteralValue("");
          sll.insertElementAt(es, 0);
        }
        break;
      }
      if (!(e instanceof MethodInvocation) || !"append".equals(((MethodInvocation) e).getName().toString())
          || ((MethodInvocation) e).arguments().size() == 0)
        return null;
      final Expression a = (Expression) ((MethodInvocation) e).arguments().get(0);
      sll.insertElementAt(addParenthesisIfNeeded(a), 0);
      hs = isString(a) || hs;
      r = (MethodInvocation) e;
    }
    // building a replacement
    Expression $;
    if (sll.size() == 0) {
      $ = i.getAST().newStringLiteral();
      ((StringLiteral) $).setLiteralValue("");
    } else if (sll.size() == 1)
      $ = (Expression) ASTNode.copySubtree(i.getAST(), sll.get(0));
    else {
      $ = i.getAST().newInfixExpression();
      InfixExpression t = (InfixExpression) $;
      for (final Expression e : sll.subList(0, sll.size() - 2)) {
        t.setLeftOperand((Expression) ASTNode.copySubtree(i.getAST(), e));
        t.setOperator(InfixExpression.Operator.PLUS);
        t.setRightOperand(i.getAST().newInfixExpression());
        t = (InfixExpression) t.getRightOperand();
      }
      t.setLeftOperand((Expression) ASTNode.copySubtree(i.getAST(), sll.get(sll.size() - 2)));
      t.setOperator(InfixExpression.Operator.PLUS);
      t.setRightOperand((Expression) ASTNode.copySubtree(i.getAST(), sll.get(sll.size() - 1)));
    }
    return $;
  }
  @Override String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "Use \"+\" operator in order to append strings";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}