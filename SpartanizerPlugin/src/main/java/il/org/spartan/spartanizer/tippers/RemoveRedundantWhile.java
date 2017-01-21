package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if </br>
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundantWhile extends ReplaceCurrentNode<WhileStatement> implements TipperCategory.Unite {
  private static boolean checkBlock(final ASTNode n) {
    if (n != null
        && (iz.expression(n) && !sideEffects.free(az.expression(n))
            || iz.expressionStatement(n) && !sideEffects.free(az.expressionStatement(n).getExpression())) //
        || !iz.block(n) && !iz.isVariableDeclarationStatement(n) //
        || iz.variableDeclarationStatement(n) && !checkVariableDecleration(az.variableDeclrationStatement(n)))
      return false;
    if (iz.block(n))
      for (final Statement ¢ : statements(az.block(n)))
        if (iz.expressionStatement(¢) && !sideEffects.free(az.expression(az.expressionStatement(¢).getExpression()))
            || !iz.isVariableDeclarationStatement(¢) || !checkVariableDecleration(az.variableDeclrationStatement(¢)))
          return false;
    return true;
  }

  private static boolean checkVariableDecleration(final VariableDeclarationStatement s) {
    for (final VariableDeclarationFragment ¢ : fragments(s))
      if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))
        return false;
    return true;
  }

  @Override public String description(final WhileStatement ¢) {
    return "remove :" + ¢;
  }

  @Override public ASTNode replacement(final WhileStatement ¢) {
    return ¢ == null || !sideEffects.free(¢.getExpression()) || !checkBlock(¢.getBody()) ? null : ¢.getAST().newBlock();
  }
}
