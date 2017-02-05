package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;


/** A {@link Tipper} to replace String appending using StringBuilder or
 * StringBuffer with appending using operator "+"
 * {@code String s = new StringBuilder(myName).append("'s grade is ").append(100).toString();}
 * can be replaced with {@code String s = myName + "'s grade is " + 100;}
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-11 */
public final class StringFromStringBuilder extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Idiomatic {
  // building a replacement
  private static ASTNode replacement( final MethodInvocation i,  final List<Expression> xs) {
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

  /** list of class extending Expression class, that need to be surrounded by
   * parenthesis when put out of method arguments list */
  private final Class<?>[] np = { InfixExpression.class };

  /** Adds parenthesis to expression if needed.
   * @param x an Expression
   * @return e itself if no parenthesis needed, otherwise a
   *         ParenthesisExpression containing e */
   private Expression addParenthesisIfNeeded( final Expression x) {
    final AST a = x.getAST();
    if (!isParethesisNeeded(x))
      return x;
    final ParenthesizedExpression $ = a.newParenthesizedExpression();
    $.setExpression((Expression) ASTNode.copySubtree(a, x));
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "Use \"+\" operator to concatenate strings";
  }

  /** Checks if an expression need parenthesis in order to interpreted correctly
   * @param x an Expression
   * @return whether or not this expression need parenthesis when put together
   *         with other expressions in infix expression. There could be non
   *         explicit parenthesis if the expression is located in an arguments
   *         list, so making it a part of infix expression require additional
   *         parenthesis */
  private boolean isParethesisNeeded(final Expression x) {
    return Stream.of(np).anyMatch(λ -> λ.isInstance(x));
  }

  @Override public ASTNode replacement( final MethodInvocation i) {
    if (!"toString".equals(i.getName() + ""))
      return null;
    final List<Expression> $ = new ArrayList<>();
    MethodInvocation r = i;
    for (boolean hs = false;;) {
      final Expression e = r.getExpression();
      final ClassInstanceCreation c = az.classInstanceCreation(e);
      if (c != null) {
        final String t = c.getType() + "";
        if (!"StringBuffer".equals(t) && !"StringBuilder".equals(t))
          return null;
        if (!c.arguments().isEmpty() && "StringBuilder".equals(t)) {
          final Expression a = onlyOne(arguments(c));
          if (a == null)
            return null;
          $.add(0, addParenthesisIfNeeded(a));
          hs |= iz.stringLiteral(a);
        }
        if (!hs)
          $.add(0, make.makeEmptyString(e));
        break;
      }
      final MethodInvocation mi = az.methodInvocation(e);
      if (mi == null || !"append".equals(mi.getName() + "") || mi.arguments().isEmpty())
        return null;
      final Expression a = onlyOne(arguments(mi));
      if (a == null)
        return null;
      $.add(0, addParenthesisIfNeeded(a));
      hs |= iz.stringLiteral(a);
      r = mi;
    }
    return replacement(i, $);
  }
}