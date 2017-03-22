package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Trait with useful fluent conditions on {@link MethodDeclaration}
 * @author Ori Marcovitch
 * @since Dec 8, 2016 */
public interface MethodPatternUtilitiesTrait {
  default boolean notEmpty(final MethodDeclaration ¢) {
    return statements(¢) != null && !empty(¢);
  }

  default boolean noBody(final MethodDeclaration ¢) {
    return body(¢) == null;
  }

  default boolean empty(final MethodDeclaration ¢) {
    return statements(¢) != null && statements(¢).isEmpty();
  }

  default boolean hazOneParameter(final MethodDeclaration ¢) {
    return parameters(¢) != null && parameters(¢).size() == 1;
  }

  default boolean hazOneStatement(final MethodDeclaration ¢) {
    return statements(¢) != null && statements(¢).size() == 1;
  }

  default boolean hazTwoStatements(final MethodDeclaration ¢) {
    return statements(¢) != null && statements(¢).size() == 2;
  }

  default boolean hazAtLeastTwoStatements(final MethodDeclaration ¢) {
    return statements(¢) != null && statements(¢).size() >= 2;
  }

  @NotNull default Collection<ReturnStatement> returnStatements(final MethodDeclaration ¢) {
    return descendants.whoseClassIs(ReturnStatement.class).from(¢);
  }

  default boolean hazNoParameters(final MethodDeclaration ¢) {
    return parameters(¢).isEmpty();
  }

  default boolean hazAtLeastTwoParameters(final MethodDeclaration ¢) {
    return parameters(¢) != null && parameters(¢).size() >= 2;
  }

  default boolean hazParameters(final MethodDeclaration ¢) {
    return !hazNoParameters(¢);
  }

  default Statement onlyStatement(final MethodDeclaration ¢) {
    return onlyOne(statements(¢));
  }

  default Statement onlyStatement(final SynchronizedStatement ¢) {
    return onlyOne(statements(¢));
  }

  default SingleVariableDeclaration onlyParameter(final MethodDeclaration ¢) {
    return onlyOne(parameters(¢));
  }

  default boolean notConstructor(final MethodDeclaration ¢) {
    return !iz.constructor(¢);
  }

  default boolean notStatic(@NotNull final MethodDeclaration ¢) {
    return !iz.static¢(¢);
  }

  default boolean returnTypeNotVoid(final MethodDeclaration ¢) {
    return !iz.voidType(returnType(¢));
  }

  default boolean returnTypeSameAsParameter(@Nullable final MethodDeclaration ¢) {
    return ¢ != null && (type(onlyParameter(¢)) + "").equals(returnType(¢) + "");
  }

  default boolean returnTypeSameAs(@Nullable final MethodDeclaration ¢, @Nullable final Type t) {
    return ¢ != null && t != null && (t + "").equals(returnType(¢) + "");
  }

  default boolean same(@Nullable final ASTNode n, @Nullable final ASTNode b) {
    return n != null && b != null && (n + "").equals(b + "");
  }

  default boolean returnsParam(final MethodDeclaration ¢) {
    return safeEquals(identifier(az.name(expression(az.returnStatement(lastStatement(¢))))), identifier(name(onlyParameter(¢))));
  }

  default ASTNode lastStatement(final MethodDeclaration ¢) {
    return last(statements(¢));
  }

  default boolean returnsThis(final MethodDeclaration ¢) {
    return iz.thisExpression(expression(az.returnStatement(onlyStatement(¢))));
  }

  default boolean lastReturnsThis(final MethodDeclaration ¢) {
    return iz.thisExpression(expression(az.returnStatement(lastStatement(¢))));
  }

  default boolean returnTypeSameAsClass(final MethodDeclaration ¢) {
    return identifier(name(yieldAncestors.untilContainingType().from(¢))).equals(returnType(¢) + "");
  }

  default Statement firstStatement(final MethodDeclaration ¢) {
    return first(statements(¢));
  }

  default Statement secondStatement(final MethodDeclaration ¢) {
    return second(statements(¢));
  }

  default boolean safeEquals(@Nullable final Object o1, @Nullable final Object o2) {
    return o1 != null && o2 != null && o1.equals(o2);
  }

  default Statement onlySynchronizedStatementStatement(final MethodDeclaration ¢) {
    return onlyStatement(az.synchronizedStatement(onlyStatement(¢)));
  }

  NanoPatternContainer<Expression> setterTippers = new NanoPatternContainer<Expression>() {
    static final long serialVersionUID = 1L;
    {
      add("this.$N1 = $N2", "", "");
      add("this.$N1 = $L", "", "");
      add("$N1 = $N2", "", "");
      add("$N1 = $L", "", "");
    }
  };

  default boolean setter(final MethodDeclaration ¢) {
    final List<String> $ = parametersNames(¢);
    return statements(¢).stream()
        .allMatch(λ -> setterTippers.canTip(expression(λ)) && isRightSideParameterOrLiteral(right(az.assignment(expression(λ))), $));
  }

  default boolean isRightSideParameterOrLiteral(final Expression $, @NotNull final Collection<String> paramNames) {
    return iz.literal($) || paramNames.contains(identifier(az.name($)));
  }

  default MethodDeclaration withoutLast(final MethodDeclaration ¢) {
    final MethodDeclaration $ = copy.of(¢);
    statements($).remove(statements($).size() - 1);
    return $;
  }

  default boolean getter(final MethodDeclaration ¢) {
    return getterTippers.canTip(onlyStatement(¢));
  }

  NanoPatternContainer<Statement> getterTippers = //
      new NanoPatternContainer<Statement>().add("return $N;").add("return this.$N;").add("return ($T)$N;").add("return ($T)this.$N;");
}
