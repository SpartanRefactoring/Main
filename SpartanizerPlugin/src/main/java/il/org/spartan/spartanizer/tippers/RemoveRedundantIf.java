package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.extract.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if </br>
 * it doesn't have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundantIf extends ReplaceCurrentNode<IfStatement> implements Kind.Collapse {
  private static boolean checkVariableDecleration(VariableDeclarationStatement s) {
    for (VariableDeclarationFragment ¢ : step.fragments(s))
      if (¢.getInitializer() != null && haz.sideEffects(¢.getInitializer()))
        return false;
    return true;
  }

  private boolean checkBlock(ASTNode n) {
    if (n != null
        && (iz.expression(n) && haz.sideEffects(az.expression(n))
            || iz.expressionStatement(n) && haz.sideEffects(az.expressionStatement(n).getExpression())) //
        || !iz.block(n) && !iz.isVariableDeclarationStatement(n) //
        || (iz.variableDeclarationStatement(n) && !checkVariableDecleration(az.variableDeclrationStatement(n))))
      return false;
    if (iz.block(n))
      for (Statement ¢ : statements(az.block(n)))
        if (iz.expressionStatement(¢) && haz.sideEffects(az.expression(az.expressionStatement(¢).getExpression()))
            || !iz.isVariableDeclarationStatement(¢)
            || iz.variableDeclarationStatement(¢) && !checkVariableDecleration(az.variableDeclrationStatement(¢)))
          return false;
    return true;
  }

  @Override public ASTNode replacement(IfStatement s) {
    if (s == null)
      return null;
    boolean condition = !haz.sideEffects(s.getExpression());
    boolean then = checkBlock(s.getThenStatement());
    boolean elze = checkBlock(s.getElseStatement());
    return condition && (then && (elze || s.getElseStatement() == null)) ? s.getAST().newBlock()
        : !condition || !then || elze || s.getElseStatement() == null ? null
            : subject.pair(duplicate.of(s.getElseStatement()), null).toNot(duplicate.of(s.getExpression()));
  }

  @Override public String description(IfStatement ¢) {
    return "remove :" + ¢;
  }
}
