package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since Dec 8, 2016 */
public interface MethodPatternUtilitiesTrait {
  default boolean oneParameter(final MethodDeclaration ¢) {
    return parameters(¢) != null && parameters(¢).size() == 1;
  }

  default boolean hazOneStatement(final MethodDeclaration ¢) {
    return statements(¢) != null && statements(¢).size() == 1;
  }

  default List<ReturnStatement> returnStatements(final MethodDeclaration ¢) {
    return searchDescendants.forClass(ReturnStatement.class).from(¢);
  }

  default boolean hazNoParams(MethodDeclaration ¢) {
    return parameters(¢).isEmpty();
  }

  default Statement onlyStatement(MethodDeclaration ¢) {
    return onlyOne(statements(¢));
  }

  default SingleVariableDeclaration onlyParameter(MethodDeclaration ¢) {
    return onlyOne(parameters(¢));
  }

  default boolean notConstructor(final MethodDeclaration ¢) {
    return !iz.constructor(¢);
  }

  default boolean notVoid(final MethodDeclaration ¢) {
    return !iz.voidType(returnType(¢));
  }
}
