package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.*;

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
