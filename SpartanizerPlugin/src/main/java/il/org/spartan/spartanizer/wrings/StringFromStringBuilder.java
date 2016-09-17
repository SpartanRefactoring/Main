package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** A {@link Wring} to replace String appending using StringBuilder or
 * StringBuffer with appending using operator "+"
 * <code>String s = new StringBuilder(myName).append("'s grade is ").append(100).toString();</code>
 * can be replaced with <code>String s = myName + "'s grade is " + 100;</code>
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-11 */
public class StringFromStringBuilder extends ReplaceCurrentNode<MethodInvocation> implements Kind.SyntacticBaggage {
  // building a replacement
  private static ASTNode replacement(final MethodInvocation i, final List<Expression> xs) {
    if (xs.isEmpty())
      return make.makeEmptyString(i);
    if (xs.size() == 1)
      return ASTNode.copySubtree(i.getAST(), xs.get(0));
    final InfixExpression $ = i.getAST().newInfixExpression();
    InfixExpression t = $;
    for (final Expression ¢ : xs.subList(0, xs.size() - 2)) {
      t.setLeftOperand((Expression) ASTNode.copySubtree(i.getAST(), ¢));
      t.setOperator(PLUS2);
      t.setRightOperand(i.getAST().newInfixExpression());
      t = (InfixExpression) t.getRightOperand();
    }
    t.setLeftOperand((Expression) ASTNode.copySubtree(i.getAST(), xs.get(xs.size() - 2)));
    t.setOperator(PLUS2);
    t.setRightOperand((Expression) ASTNode.copySubtree(i.getAST(), xs.get(xs.size() - 1)));
    return $;
  }

  // list of class extending Expression class, that need to be surrounded by
  // parenthesis
  // when put out of method arguments list
  private final Class<?>[] np = { InfixExpression.class };

  @Override public ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(i.getName() + ""))
      return null;
    final List<Expression> terms = new ArrayList<>();
    MethodInvocation r = i;
    boolean hs = false;
    // collecting strings from append method arguments list and from class
    // instance creation arguments list
    while (true) {
      final Expression e = r.getExpression();
      if (e instanceof ClassInstanceCreation) {
        final String t = ((ClassInstanceCreation) e).getType() + "";
        if (!"StringBuffer".equals(t) && !"StringBuilder".equals(t))
          return null;
        if (!((ClassInstanceCreation) e).arguments().isEmpty() && "StringBuilder".equals(t)) {
          final Expression a = (Expression) ((ClassInstanceCreation) e).arguments().get(0);
          terms.add(0, addParenthesisIfNeeded(a));
          hs |= iz.stringLiteral(a);
        }
        if (!hs)
          terms.add(0, make.makeEmptyString(e));
        break;
      }
      if (!(e instanceof MethodInvocation) || !"append".equals(((MethodInvocation) e).getName() + "") || ((MethodInvocation) e).arguments().isEmpty())
        return null;
      final Expression a = (Expression) ((MethodInvocation) e).arguments().get(0);
      terms.add(0, addParenthesisIfNeeded(a));
      hs |= iz.stringLiteral(a);
      r = (MethodInvocation) e;
    }
    return replacement(i, terms);
  }

  @Override protected String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "Use \"+\" operator to concatenate strings";
  }

  /** Adds parenthesis to expression if needed.
   * @param x an Expression
   * @return e itself if no parenthesis needed, otherwise a
   *         ParenthesisExpression containing e */
  private Expression addParenthesisIfNeeded(final Expression x) {
    final AST a = x.getAST();
    if (!isParethesisNeeded(x))
      return x;
    final ParenthesizedExpression $ = a.newParenthesizedExpression();
    $.setExpression((Expression) ASTNode.copySubtree(a, x));
    return $;
  }

  /** Checks if an expression need parenthesis in order to interpreted correctly
   * @param x an Expression
   * @return whether or not this expression need parenthesis when put together
   *         with other expressions in infix expression. There could be non
   *         explicit parenthesis if the expression is located in an arguments
   *         list, so making it a part of infix expression require additional
   *         parenthesis */
  private boolean isParethesisNeeded(final Expression x) {
    for (final Class<?> ¢ : np)
      if (¢.isInstance(x))
        return true;
    return false;
  }
}