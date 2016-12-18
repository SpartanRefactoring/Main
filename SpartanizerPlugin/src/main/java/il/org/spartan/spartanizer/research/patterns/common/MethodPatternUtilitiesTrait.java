package il.org.spartan.spartanizer.research.patterns.common;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since Dec 8, 2016 */
public interface MethodPatternUtilitiesTrait {
  default boolean notEmpty(final MethodDeclaration ¢) {
    return body(¢) != null;
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

  default List<ReturnStatement> returnStatements(final MethodDeclaration ¢) {
    return searchDescendants.forClass(ReturnStatement.class).from(¢);
  }

  default boolean hazNoParameters(final MethodDeclaration ¢) {
    return parameters(¢).isEmpty();
  }

  default boolean hazParameters(final MethodDeclaration ¢) {
    return !hazNoParameters(¢);
  }

  default Statement onlyStatement(final MethodDeclaration ¢) {
    return onlyOne(statements(¢));
  }

  default SingleVariableDeclaration onlyParameter(final MethodDeclaration ¢) {
    return onlyOne(parameters(¢));
  }

  default boolean notConstructor(final MethodDeclaration ¢) {
    return !iz.constructor(¢);
  }

  default boolean notVoid(final MethodDeclaration ¢) {
    return !iz.voidType(returnType(¢));
  }

  default boolean returnTypeSameAsParameter(final MethodDeclaration ¢) {
    return (type(onlyParameter(¢)) + "").equals(returnType(¢) + "");
  }

  default boolean returnTypeSameAs(final MethodDeclaration ¢, final Type t) {
    return ¢ != null && t != null && (t + "").equals(returnType(¢) + "");
  }

  default boolean same(final ASTNode n, final ASTNode b) {
    return n != null && b != null && (n + "").equals(b + "");
  }

  default boolean returnsParam(final MethodDeclaration ¢) {
    return iz.returnStatement(lastStatement(¢))
        && identifier(az.name(expression(az.returnStatement(lastStatement(¢))))).equals(identifier(name(onlyParameter(¢))));
  }

  /** @param ¢
   * @return */
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
    return identifier(name(searchAncestors.forContainingType().from(¢))).equals(returnType(¢) + "");
  }

  default Statement firstStatement(final MethodDeclaration ¢) {
    return first(statements(¢));
  }
}
