package il.org.spartan.refactoring.wring;

import java.util.Vector;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to replace String appending using StringBuilder or StringBuffer with
 * appending using operator "+"
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
  private Class<?>[] np = { InfixExpression.class };
  /**
   * Checks if an expression need parenthesis in order to interpreted correctly
   * 
   * @param e
   *          an Expression
   * @return whether or not this expression need parenthesis when put together
   *         with other expressions in infix expression. There could be non
   *         explicit parenthesis if the expression is located in an arguments
   *         list, so making it a part of infix expression require additional
   *         parenthesis
   */
  private boolean isParethesisNeeded(Expression e) {
    for (Class<?> c : np) {
      if (c.isInstance(e)) {
        return true;
      }
    }
    return false;
  }
  /**
   * Adds parenthesis to expression if needed.
   * 
   * @param e
   *          an Expression
   * @return e itself if no parenthesis needed, otherwise a
   *         ParenthesisExpression containing e
   */
  private Expression addParenthesisIfNeeded(Expression e) {
    AST a = e.getAST();
    if (!isParethesisNeeded(e)) {
      return e;
    }
    ParenthesizedExpression pe = a.newParenthesizedExpression();
    pe.setExpression((Expression) Expression.copySubtree(a, e));
    return pe;
  }
  /**
   * @param e
   *          an Expression
   * @return true iff e is a String
   */
  private boolean isString(Expression e) {
    return e instanceof StringLiteral;
  }
  @Override
  ASTNode replacement(final MethodInvocation n) {
    if (!n.getName().toString().equals("toString")) {
      return null;
    }
    Vector<Expression> sll = new Vector<>();
    MethodInvocation r = n;
    boolean hs = false;
    // collecting strings from append method arguments list and from class
    // instance creation arguments list
    while (true) {
      Expression e = r.getExpression();
      if (e instanceof ClassInstanceCreation) {
        String t = ((ClassInstanceCreation) e).getType().toString();
        if (t.equals("StringBuffer") || t.equals("StringBuilder")) {
          if (!((ClassInstanceCreation) e).arguments().isEmpty() && t.equals("StringBuilder")) {
            Expression a = (Expression) ((ClassInstanceCreation) e).arguments().get(0);
            sll.insertElementAt(addParenthesisIfNeeded(a), 0);
            hs = isString(a) ? true : hs;
          }
          if (!hs) {
            // creating a "" string literal to ensure final value interpreted as
            // a string.
            // this expression is created twice, maybe unnecessarily
            StringLiteral es = n.getAST().newStringLiteral();
            es.setLiteralValue("");
            sll.insertElementAt(es, 0);
          }
          break;
        }
        return null;
      } else if (e instanceof MethodInvocation) {
        if (!((MethodInvocation) e).getName().toString().equals("append")
            || ((MethodInvocation) e).arguments().size() == 0) {
          return null;
        }
        Expression a = (Expression) ((MethodInvocation) e).arguments().get(0);
        sll.insertElementAt(addParenthesisIfNeeded(a), 0);
        hs = isString(a) ? true : hs;
        r = (MethodInvocation) e;
      } else {
        return null;
      }
    }
    // building a replacement
    Expression $;
    if (sll.size() == 0) {
      $ = n.getAST().newStringLiteral();
      ((StringLiteral) $).setLiteralValue("");
    } else if (sll.size() == 1) {
      $ = (Expression) Expression.copySubtree(n.getAST(), sll.get(0));
    } else {
      $ = n.getAST().newInfixExpression();
      InfixExpression t = (InfixExpression) $;
      for (Expression e : sll.subList(0, sll.size() - 2)) {
        t.setLeftOperand((Expression) Expression.copySubtree(n.getAST(), e));
        t.setOperator(InfixExpression.Operator.PLUS);
        t.setRightOperand(n.getAST().newInfixExpression());
        t = (InfixExpression) t.getRightOperand();
      }
      t.setLeftOperand((Expression) Expression.copySubtree(n.getAST(), sll.get(sll.size() - 2)));
      t.setOperator(InfixExpression.Operator.PLUS);
      t.setRightOperand((Expression) Expression.copySubtree(n.getAST(), sll.get(sll.size() - 1)));
    }
    return $;
  }
  @Override
  String description(final MethodInvocation n) {
    return "Use \"+\" operator in order to append strings";
  }
  @Override
  WringGroup wringGroup() {
    return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
  }
}