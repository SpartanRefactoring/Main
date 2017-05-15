package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A {@link Tipper} to replace String appending using StringBuilder or
 * StringBuffer with appending using operator "+"
 * {@code String s = new StringBuilder(myName).append("'s grade is ").append(100).toString();}
 * can be replaced with {@code String s = myName + "'s grade is " + 100;}
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-11 */
public final class StringFromStringBuilder extends ClassInstanceCreationPattern //
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0x27A02FA5B2C416E2L;
  private Expression simplification;
  private List<MethodInvocation> invocations;

  public StringFromStringBuilder() {
    andAlso("StringBuilder/StringBuffer instance creation", () -> is.in(name, "StringBuilder", "StringBuffer"));
    andAlso("Converted to String", () -> {
      invocations = ancestors.until((p, c) -> !iz.methodInvocation(c) || p != null && p != az.methodInvocation(c).getExpression()).from(parent)
          .stream().map(az::methodInvocation).collect(toList());
      final Expression $ = az.expression((invocations.isEmpty() ? current : the.lastOf(invocations)).getParent());
      return !invocations.isEmpty() && "toString".equals(the.lastOf(invocations).getName().getIdentifier()) || not.nil($) && iz.infixPlus($)
          && (iz.stringLiteral(az.infixExpression($).getLeftOperand()) || iz.stringLiteral(az.infixExpression($).getRightOperand()));
    });
    andAlso("All invocations are append/toString",
        () -> invocations.stream().allMatch(λ -> is.in(λ.getName().getIdentifier(), "append", "toString")));
    andAlso("All append invocation have one parameter",
        () -> invocations.stream().filter(λ -> "append".equals(λ.getName().getIdentifier())).allMatch(λ -> λ.arguments().size() == 1));
    andAlso("All to toString have zero parameters",
        () -> invocations.stream().filter(λ -> "toString".equals(λ.getName().getIdentifier())).allMatch(λ -> λ.arguments().isEmpty()));
    andAlso("Can be simplified", () -> not.nil(simplification = simplification()));
  }
  @Override public String description() {
    return "Use \"+\" operator to concatenate strings";
  }
  @Override public Examples examples() {
    return convert("x.print(new StringBuilder(\"Description:\\t\").append(x+1).append(\"\\n\").toString())") //
        .to("x.print(\"Description:\\t\" + (x+1) + \"\\n\")") //
        .convert("x.print(new StringBuilder(1).append(x+1).append(\"\\n\").toString())") //
        .to("x.print(\"\" + 1 + (x+1) + \"\\n\")") //
        .ignores("x.print(new StringBuilder(\"Description:\\t\").append(x+1).append(\"\\n\"))") //
        .ignores("new StringBuilder(\"Description:\\t\").append(x,y,z)") //
        .ignores("new StringBuilder(\"Description:\\t\").toString(x)");
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(invocations.isEmpty() ? current : the.lastOf(invocations), simplification, g);
    return r;
  }
  @Override protected ASTNode[] span() {
    return new Expression[] { invocations.isEmpty() ? current : the.lastOf(invocations) };
  }
  @Override protected ASTNode highlight() {
    return invocations.isEmpty() ? current : the.lastOf(invocations);
  }
  private Expression simplification() {
    final List<Expression> $ = arguments(current.arguments());
    $.addAll(invocations.stream().reduce(an.empty.list(), (l, i) -> {
      l.addAll(arguments(i.arguments()));
      return l;
    }, (l1, l2) -> {
      l1.addAll(l2);
      return l1;
    }));
    if (needPreliminaryStringSafe($))
      $.add(0, make.emptyString(current));
    return $.isEmpty() ? make.emptyString(current) : $.size() == 1 ? copy.of(the.headOf($)) : subject.operands($).to(Operator.PLUS);
  }
  public static boolean needPreliminaryStringUnsafe(final List<Expression> ¢) {
    return ¢.isEmpty() || !iz.stringLiteral(the.headOf(¢)) && !iz.name(the.headOf(¢)) && !iz.methodInvocation(the.headOf(¢));
  }
  public static boolean needPreliminaryStringSafe(final List<Expression> ¢) {
    return ¢.isEmpty() || !iz.stringLiteral(the.headOf(¢));
  }
  private static List<Expression> arguments(final List<?> argumentz) {
    return argumentz.stream().filter(λ -> λ instanceof Expression).map(λ -> addParenthesisIfNeeded((Expression) λ)).collect(toList());
  }
}
