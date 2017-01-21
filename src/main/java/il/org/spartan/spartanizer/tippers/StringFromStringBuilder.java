package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to replace String appending using StringBuilder or
 * StringBuffer with appending using operator "+"
 * <code>String s = new StringBuilder(myName).append("'s grade is ").append(100).toString();</code>
 * can be replaced with <code>String s = myName + "'s grade is " + 100;</code>
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-11 */
public final class StringFromStringBuilder extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Idiomatic {
  // building a replacement
  private static ASTNode replacement(final MethodInvocation i, final List<Expression> xs) {
    if (xs.isEmpty())
      return make.makeEmptyString(i);
    if (xs.size() == 1)
      return ASTNode.copySubtree(i.getAST(), first(xs));
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
    t.setRightOperand((Expression) ASTNode.copySubtree(i.getAST(), last(xs)));
    return $;
  }

  // list of class extending Expression class, that need to be surrounded by
  // parenthesis
  // when put out of method arguments list
  private final Class<?>[] np = { InfixExpression.class };

  @Override public String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "Use \"+\" operator to concatenate strings";
  }

  @Override public ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(i.getName() + ""))
      return null;
    final List<Expression> $ = new ArrayList<>();
    MethodInvocation r = i;
    for (boolean hs = false;;) {
      final Expression e = r.getExpression();
      if (e instanceof ClassInstanceCreation) {
        final String t = ((ClassInstanceCreation) e).getType() + "";
        if (!"StringBuffer".equals(t) && !"StringBuilder".equals(t))
          return null;
        if (!((ClassInstanceCreation) e).arguments().isEmpty() && "StringBuilder".equals(t)) {
          final Expression a = first(arguments((ClassInstanceCreation) e));
          $.add(0, addParenthesisIfNeeded(a));
          hs |= iz.stringLiteral(a);
        }
        if (!hs)
          $.add(0, make.makeEmptyString(e));
        break;
      }
      if (!(e instanceof MethodInvocation) || !"append".equals(((MethodInvocation) e).getName() + "") || ((MethodInvocation) e).arguments().isEmpty())
        return null;
      final Expression a = first(arguments((MethodInvocation) e));
      $.add(0, addParenthesisIfNeeded(a));
      hs |= iz.stringLiteral(a);
      r = (MethodInvocation) e;
    }
    return replacement(i, $);
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