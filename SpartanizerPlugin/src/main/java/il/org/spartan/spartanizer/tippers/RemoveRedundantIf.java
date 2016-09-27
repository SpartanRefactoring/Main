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
  private boolean checkVariableDecleration(VariableDeclarationStatement n) {
    List<VariableDeclarationFragment> lst = n.fragments();
    for (VariableDeclarationFragment f : lst) {
      if (f.getInitializer() != null && !sideEffects.free(f.getInitializer()))
        return false;
    }
    return true;
  }

  private boolean checkBlock(ASTNode n) {
    if (n != null
        && (iz.expression(n) && !sideEffects.free(az.expression(n))
            || iz.expressionStatement(n) && !sideEffects.free(az.expressionStatement(n).getExpression())) //
        || !iz.block(n) && !iz.isVariableDeclarationStatement(n) //
        || (iz.variableDeclarationStatement(n) && !checkVariableDecleration(az.variableDeclrationStatement(n))))
      return false;
    if (iz.block(n)) {
      for (Statement s : statements(az.block(n))) {
        if (iz.expressionStatement(s) && !sideEffects.free(az.expression(az.expressionStatement(s).getExpression())))
          return false;
        if (!iz.isVariableDeclarationStatement(s)
            || iz.variableDeclarationStatement(s) && !checkVariableDecleration(az.variableDeclrationStatement(s)))
          return false;
      }
    }
    return true;
  }

  @Override public ASTNode replacement(IfStatement n) {
    if (n == null)
      return null;
    boolean condition = sideEffects.free(n.getExpression());
    boolean then = checkBlock(n.getThenStatement());
    boolean elze = checkBlock(n.getElseStatement());
    if (condition && then && elze || (condition && then && n.getElseStatement() == null))
      return n.getAST().newBlock();
    if (condition && then && !elze && (n.getElseStatement() != null)) {
      return subject.pair(duplicate.of(n.getElseStatement()), null).toNot(duplicate.of(n.getExpression()));
    }
    return null;
  }

  @Override public String description(IfStatement n) {
    return "remove :" + n;
  }
}
