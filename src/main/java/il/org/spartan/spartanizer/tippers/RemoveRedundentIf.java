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

  
  private boolean checkBlock(ASTNode n){
    if(n!= null &&
        (iz.expression(n) && !sideEffects.free(az.expression(n))
        || iz.expressionStatement(n) && !sideEffects.free(az.expressionStatement(n).getExpression())))
      return false;
    if (iz.block(n)) {
      List<Statement> lst = az.block(n).statements();
      for (Statement s : lst) {
        if (az.expressionStatement(s) != null) {
          if (!sideEffects.free(az.expression(az.expressionStatement(s).getExpression())))
            return false;
        }
      }
    }
    return true;
  }
  @Override public ASTNode replacement(IfStatement n) {
    if(n==null)
      return null;
    boolean condition = sideEffects.free(n.getExpression());
    boolean then = checkBlock(n.getThenStatement());
    boolean elze = checkBlock(n.getElseStatement());

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
