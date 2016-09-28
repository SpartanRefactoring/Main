package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

public class RemoveRedundent {
  public static boolean checkVariableDecleration(VariableDeclarationStatement s) {
    List<VariableDeclarationFragment> lst = step.fragments(s);
    for (VariableDeclarationFragment ¢ : lst)
      if (¢.getInitializer() != null && haz.sideEffects(¢.getInitializer()))
        return false;
    return true;
  }

  public static boolean checkBlock(ASTNode n) {
    if (n != null
        && (iz.expression(n) && haz.sideEffects(az.expression(n))
            || iz.expressionStatement(n) && haz.sideEffects(az.expressionStatement(n).getExpression())) //
        || !iz.block(n) && !iz.isVariableDeclarationStatement(n) //
        || (iz.variableDeclarationStatement(n) && !checkVariableDecleration(az.variableDeclrationStatement(n))))
      return false;
    if (iz.block(n))
      for (Statement ¢ : step.statements(az.block(n)))
        if (iz.expressionStatement(¢) && haz.sideEffects(az.expression(az.expressionStatement(¢).getExpression()))
            || !iz.isVariableDeclarationStatement(¢)
            || iz.variableDeclarationStatement(¢) && !checkVariableDecleration(az.variableDeclrationStatement(¢)))
          return false;
    return true;
  }

  public static boolean checkListOfExpressions(List<Expression> xs) {
    for (Expression ¢ : xs) {
      final Expression e = ¢;
      if (haz.sideEffects(e))
        return false;
    }
    return true;
  }
}
