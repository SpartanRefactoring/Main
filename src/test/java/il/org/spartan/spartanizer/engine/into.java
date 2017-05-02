package il.org.spartan.spartanizer.engine;

import static fluent.ly.azzert.*;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
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
    assert conditionalExpression != null;
    assert $ != null;
    azzert.that(conditionalExpression, $, instanceOf(ConditionalExpression.class));
    return (ConditionalExpression) $;
  }

  /** @param p a {@link String} that represents a Java Compilation unit
   * @return {@link CompilationUnit} data structure representing the
   *         parameter. */
  public static CompilationUnit cu(final String cu) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.from(cu);
  }

  /** @param p a {@link String} that represents a Java Compilation unit
   * @return {@link CompilationUnit} data structure representing the
   *         parameter. */
  public static CompilationUnit cuWithBinding(final String cu) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(cu).createAST(null);
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
    assert methodDelclaration != null;
    return findFirst.instanceOf(MethodDeclaration.class).in(WrapIntoComilationUnit.Method.intoCompilationUnit(methodDelclaration));
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
  public static Collection<Expression> es(final String... expressions) {
    return Stream.of(expressions).map(into::e).collect(toList());
  }

  /** Convert a given {@link String} into an {@link InfixExpression}, or fail
   * the current test, if such a conversion is not possible
   * @param expression a {@link String} that represents a Java expression
   * @return an {@link InfixExpression} data structure representing the
   *         parameter. */
  public static InfixExpression i(final String expression) {
    return (InfixExpression) e(expression);
  }

  public static MethodDeclaration m(final String p) {
    return findFirst.instanceOf(MethodDeclaration.class).in(makeAST.CLASS_BODY_DECLARATIONS.from(p));
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
    assert statement != null;
    final ASTNode $ = makeAST.STATEMENTS.from(statement);
    assert statement != null;
    assert $ != null;
    azzert.that(statement, $, instanceOf(Statement.class));
    return (Statement) $;
  }

  public static Type t(final String codeFragment) {
    return findFirst.instanceOf(Type.class).in(s(codeFragment));
  }
}