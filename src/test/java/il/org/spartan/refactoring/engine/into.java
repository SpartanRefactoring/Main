package il.org.spartan.refactoring.engine;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.spartanizations.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum into {
  ;
  /** Convert a given {@link String} into an {@link Assignment}, or fail the
   * current test, if such a conversion is not possible
   * @param expression a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter. */
  public static Assignment a(final String expression) {
    return (Assignment) e(expression);
  }

  /** Convert a given {@link String} into an {@link ConditionalExpression}, or
   * fail the current test, if such a conversion is not possible
   * @param conditionalExpression a {@link String} that represents a
   *        "conditional" (also known as "ternary") expression.
   * @return an {@link Statement} data structure representing the parameter. */
  public static ConditionalExpression c(final String conditionalExpression) {
    final Expression $ = e(conditionalExpression);
    azzert.notNull(conditionalExpression, $);
    azzert.that(conditionalExpression, $, instanceOf(ConditionalExpression.class));
    return (ConditionalExpression) $;
  }

  /** Convert a given {@link String} into an {@link MethodDeclaration} by
   * appropriately wrapping it with text to make it a reasonably looking
   * {@link CompilationUnit}, parsing it, and then extracting the first method
   * in it. possible
   * @param methodDelclaration a {@link String} that represents a Java method
   *        declaration
   * @return an {@link MethodDeclaration} data structure representing the
   *         parameter. */
  public static MethodDeclaration d(final String methodDelclaration) {
    azzert.notNull(methodDelclaration);
    return extract.firstMethodDeclaration(Wrap.Method.intoCompilationUnit(methodDelclaration));
  }

  /** Convert a given {@link String} into an {@link Expression}, or fail the
   * current test, if such a conversion is not possible
   * @param expression a {@link String} that represents a Java expression
   * @return an {@link Expression} data structure representing the parameter. */
  public static Expression e(final String expression) {
    return (Expression) makeAST.EXPRESSION.from(expression);
  }

  /** Convert an array of {@link String} into a {@link List} of
   * {@link Expression}, or fail the current test, if such a conversion is not
   * possible
   * @param expressions an array of {@link String}s, each representing a Java
   *        expression
   * @return a {@link List} of {@link Expression} data structures, each
   *         representing an element of the input. */
  public static List<Expression> es(final String... expressions) {
    final List<Expression> $ = new ArrayList<>();
    for (final String expression : expressions)
      $.add(e(expression));
    return $;
  }

  /** Convert a given {@link String} into an {@link InfixExpression}, or fail
   * the current test, if such a conversion is not possible
   * @param expression a {@link String} that represents a Java expression
   * @return an {@link InfixExpression} data structure representing the
   *         parameter. */
  public static InfixExpression i(final String expression) {
    return (InfixExpression) e(expression);
  }

  /** Convert a given {@link String} into an {@link PrefixExpression}, or fail
   * the current test, if such a conversion is not possible
   * @param expression a {@link String} that represents a Java expression
   * @return a {@link PrefixExpression} data structure representing the
   *         parameter. */
  public static PrefixExpression p(final String expression) {
    return (PrefixExpression) e(expression);
  }

  /** Convert a given {@link String} into an {@link Statement}, or fail the
   * current test, if such a conversion is not possible
   * @param statement a {@link String} that represents a Java statement
   * @return an {@link Statement} data structure representing the parameter. */
  public static Statement s(final String statement) {
    azzert.notNull(statement);
    final ASTNode n = makeAST.STATEMENTS.from(statement);
    azzert.notNull(statement, n);
    azzert.that(statement, n, instanceOf(Statement.class));
    return (Statement) n;
  }

  public static Type t(final String codeFragment) {
    return extract.firstType(s(codeFragment));
  }
}