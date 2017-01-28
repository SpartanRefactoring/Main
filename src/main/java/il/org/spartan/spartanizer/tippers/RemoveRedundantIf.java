package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundantIf extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.EmptyCycles {
  private static boolean checkBlock(final ASTNode n) {
    if (n != null
        && (iz.expression(n) && !sideEffects.free(az.expression(n))
            || iz.expressionStatement(n) && !sideEffects.free(expression(az.expressionStatement(n)))) //
        || !iz.block(n) && !iz.isVariableDeclarationStatement(n) //
        || iz.variableDeclarationStatement(n) && !checkVariableDecleration(az.variableDeclrationStatement(n)))
      return false;
    if (iz.block(n))
      for (final Statement ¢ : statements(az.block(n)))
        if (iz.expressionStatement(¢) && !sideEffects.free(az.expression(az.expressionStatement(¢).getExpression()))
            || !iz.isVariableDeclarationStatement(¢)
            || iz.variableDeclarationStatement(¢) && !checkVariableDecleration(az.variableDeclrationStatement(¢)))
          return false;
    return true;
  }

  private static boolean checkVariableDecleration(final VariableDeclarationStatement s) {
    return fragments(s).stream().allMatch(λ -> initializer(λ) == null || sideEffects.free(initializer(λ)));
  }

  @Override public String description(final IfStatement ¢) {
    return "remove :" + ¢;
  }

  @Override public ASTNode replacement(final IfStatement s) {
    if (s == null)
      return null;
    final boolean $ = sideEffects.free(expression(s)), then = checkBlock(s.getThenStatement()), elze = checkBlock(elze(s));
    return $ && then && (elze || elze(s) == null) ? s.getAST().newBlock()
        : !$ || !then || elze || elze(s) == null ? null : subject.pair(copy.of(elze(s)), null).toNot(copy.of(expression(s)));
  }
}
