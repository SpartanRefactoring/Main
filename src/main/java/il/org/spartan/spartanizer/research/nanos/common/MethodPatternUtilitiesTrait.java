package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 8, 2016 */
public interface MethodPatternUtilitiesTrait {
  default boolean notEmpty(final MethodDeclaration ¢) {
    return statements(¢) != null && !empty(¢);
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

  @NotNull
  default List<ReturnStatement> returnStatements(final MethodDeclaration ¢) {
    return yieldDescendants.untilClass(ReturnStatement.class).from(¢);
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

  default boolean safeEquals(@Nullable final Object o1, @Nullable final Object o2) {
    return o1 != null && o2 != null && o1.equals(o2);
  }

  default Statement onlySynchronizedStatementStatement(final MethodDeclaration ¢) {
    return onlyStatement(az.synchronizedStatement(onlyStatement(¢)));
  }
}
