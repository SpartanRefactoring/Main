package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A {@link Tipper} to replace String appending using StringBuilder or
 * StringBuffer with appending using operator "+"
 * {@code String s = new StringBuilder(myName).append("'s grade is ").append(100).toString();}
 * can be replaced with {@code String s = myName + "'s grade is " + 100;}
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-11 */
public final class StringFromStringBuilder extends MethodInvocationPattern//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0x27A02FA5B2C416E2L;
  private Expression simplification;

  public StringFromStringBuilder() {
    andAlso("Not toString", () -> !"toString".equals(name + ""));
    andAlso("Can be simplified toString", () -> not.null¢(simplification = simplification()));
  }

  private static Expression replacement(final MethodInvocation i, final List<Expression> xs) {
    if (xs.isEmpty())
      return make.emptyString(i);
    if (xs.size() == 1)
      return copy.of(first(xs));
    final InfixExpression $ = i.getAST().newInfixExpression();
    InfixExpression t = $;
    for (final Expression ¢ : xs.subList(0, xs.size() - 2)) {
      t.setLeftOperand(copy.of(¢));
      t.setOperator(PLUS2);
      t.setRightOperand(i.getAST().newInfixExpression());
      t = (InfixExpression) t.getRightOperand();
    }
    t.setLeftOperand(copy.of(the.penultimate(xs)));
    t.setOperator(PLUS2);
    t.setRightOperand(copy.of(last(xs)));
    return $;
  }

  @Override public String description() {
    return "Use \"+\" operator to concatenate strings";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current, simplification, g);
    return r;
  }

  private Expression simplification() {
    final List<Expression> $ = new ArrayList<>();
    MethodInvocation r = current;
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
          $.add(0, make.emptyString(e));
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
    return replacement(current, $);
  }

  @Override public Examples examples() {
    return null;
  }
}