package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.nanos.NanoPatternsConfiguration;
import il.org.spartan.spartanizer.research.UserDefinedTipper;

public interface NanoPatternUtil {
  static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        || body(¢) == null//
        || anyTips(NanoPatternsConfiguration.skipped, ¢);
  }
  static boolean anyTips(final Collection<JavadocMarkerNanoPattern> ps, final MethodDeclaration d) {
    return d != null && ps.stream().anyMatch(λ -> λ.check(d));
  }
  static <N extends ASTNode> boolean anyTips(final Collection<UserDefinedTipper<N>> ts, final N n) {
    return n != null && ts.stream().anyMatch(λ -> λ.check(n));
  }
  static boolean nullCheck(final Expression ¢) {
    return nullComparison(¢)//
        || nullComparisonOr.check(¢)//
            && nullCheck(right(az.infixExpression(¢)));
  }
  static boolean nullComparison(final Expression ¢) {
    return nullComparison.check(¢);
  }
  static boolean nullComparisonIncremental(final Expression ¢) {
    return nullComparisonOr.check(¢);
  }

  UserDefinedTipper<Expression> nullComparison = patternTipper("$X == null", "", "");
  UserDefinedTipper<Expression> nullComparisonOr = patternTipper("$X1 == null || $X2", "", "");
  Collection<UserDefinedTipper<Statement>> defaultReturns = as.list(patternTipper("return;", "", ""), patternTipper("return null;", "", ""));
  UserDefinedTipper<Statement> returns = patternTipper("return $X;", "", "");

  static boolean returnsDefault(final Statement ¢) {
    return anyTips(defaultReturns, ¢);
  }
  static ASTNode returnee(final Statement ¢) {
    return returns.getMatching(¢, "$X");
  }
  static Iterable<String> nullCheckees(final IfStatement ¢) {
    Expression e = expression(¢);
    final Collection<String> $ = an.empty.list();
    for (; nullComparisonIncremental(e); e = right(az.infixExpression(e)))
      $.add(left(az.infixExpression(left(az.infixExpression(e)))) + "");
    $.add(left(az.infixExpression(e)) + "");
    return $;
  }
}
