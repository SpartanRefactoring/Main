package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.*;

public interface NanoPatternUtil {
  static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        || body(¢) == null//
        || anyTips(NanoPatternsConfiguration.skipped, ¢);
  }

  static boolean anyTips(@NotNull final Collection<JavadocMarkerNanoPattern> ps, @Nullable final MethodDeclaration d) {
    return d != null && ps.stream().anyMatch(λ -> λ.check(d));
  }

  static <N extends ASTNode> boolean anyTips(@NotNull final Collection<UserDefinedTipper<N>> ts, @Nullable final N n) {
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
  Collection<UserDefinedTipper<Statement>> defaultReturns = new ArrayList<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return;", "", ""));
      add(patternTipper("return null;", "", ""));
    }
  };
  UserDefinedTipper<Statement> returns = patternTipper("return $X;", "", "");

  static boolean returnsDefault(final Statement ¢) {
    return anyTips(defaultReturns, ¢);
  }

  static ASTNode returnee(final Statement ¢) {
    return returns.getMatching(¢, "$X");
  }

  @NotNull static Iterable<String> nullCheckees(final IfStatement ¢) {
    @NotNull Expression e = expression(¢);
    @NotNull final Collection<String> $ = new ArrayList<>();
    while (nullComparisonIncremental(e)) {
      $.add(left(az.infixExpression(left(az.infixExpression(e)))) + "");
      e = right(az.infixExpression(e));
    }
    $.add(left(az.infixExpression(e)) + "");
    return $;
  }
}
