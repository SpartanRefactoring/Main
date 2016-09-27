package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * Remove if that has no impact on the code if and only if </br>
 * it doesn't have any side-effect.
 * 
 * @author Dor Ma'ayan
 * @since 2016-09-26
 *
 */
public class RemoveRedundentIf extends ReplaceCurrentNode<IfStatement> implements Kind.Collapse{

  @Override public ASTNode replacement(IfStatement n) {
    boolean condition = true;
    boolean then = true;
    boolean elze = true;
    if(n==null)
      return null;
    if (!sideEffects.free(n.getExpression()))
      condition=false;
    if(n.getThenStatement() != null &&
        (iz.expression(n.getThenStatement()) && !sideEffects.free(az.expression(n.getThenStatement()))
        || iz.expressionStatement(n.getThenStatement()) && !sideEffects.free(az.expressionStatement(n.getThenStatement()).getExpression())))
      then=false;
    if(n.getElseStatement() != null &&
        (iz.expression(n.getElseStatement()) && !sideEffects.free(az.expression(n.getElseStatement()))
        || iz.expressionStatement(n.getElseStatement()) && !sideEffects.free(az.expressionStatement(n.getElseStatement()).getExpression())))
      elze=false;
    if (iz.block(n.getThenStatement())) {
      List<Statement> lst = az.block(n.getThenStatement()).statements();
      for (Statement s : lst) {
        if (az.expressionStatement(s) != null) {
          if (!sideEffects.free(az.expression(az.expressionStatement(s).getExpression())))
            then=false;
        }
      }
    }
    if (iz.block(n.getElseStatement())) {
      List<Statement> lst = az.block(n.getElseStatement()).statements();
      for (Statement s : lst) {
        if (az.expressionStatement(s) != null) {
          if (!sideEffects.free(az.expression(az.expressionStatement(s).getExpression())))
          elze=false;
        }
      }
    }
    if(condition && then && elze || (condition && then && n.getElseStatement()==null))
      return n.getAST().newBlock();
    if(condition && then && !elze && (n.getElseStatement()!=null)){
      return subject.pair(duplicate.of(n.getElseStatement()),null).toNot(duplicate.of(n.getExpression()));
    }
    return null;
  }

  @Override public String description(IfStatement n) {
    return "remove :" + n;
  }
}
