package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * Simplify for statements as much as possible (or remove them or parts of them) if and only if </br>
 * it doesn't have any side-effect.
 * 
 * @author Dor Ma'ayan
 * @since 2016-09-26
 *
 */
public class RemoveRedundentFor extends ReplaceCurrentNode<ForStatement> implements Kind.Collapse{

  
  @SuppressWarnings("all") private boolean checkVariableDecleration(VariableDeclarationStatement n ){    
    List<VariableDeclarationFragment> lst = n.fragments();
    for(VariableDeclarationFragment f : lst){
      if(f.getInitializer()!=null && !sideEffects.free(f.getInitializer()))
        return false;
    }
    return true;
  }
  private boolean checkBlock(ASTNode n){
    if(n!= null &&
        (iz.expression(n) && !sideEffects.free(az.expression(n))
        || iz.expressionStatement(n) && !sideEffects.free(az.expressionStatement(n).getExpression())) //
        || !iz.block(n) && !iz.isVariableDeclarationStatement(n) //
        ||(iz.variableDeclarationStatement(n) && !checkVariableDecleration(az.variableDeclrationStatement(n))))
      return false;
    if (iz.block(n)) {
      List<Statement> lst = az.block(n).statements();
      for (Statement s : lst) {
        if (iz.expressionStatement(s) && !sideEffects.free(az.expression(az.expressionStatement(s).getExpression())))
            return false;
        if(!iz.isVariableDeclarationStatement(s) || iz.variableDeclarationStatement(s) && !checkVariableDecleration(az.variableDeclrationStatement(s)))
          return false;
      }
    }
    return true;
  }
  
  private boolean checkListOfExpressions(List<Expression> lst){
    for(Expression e: lst)
      if(!sideEffects.free(e))
        return false;
    return true;
  }
  @Override public ASTNode replacement(ForStatement n) {
    if(n==null)
      return null;
    boolean condition = sideEffects.free(n.getExpression());
    boolean initializers = checkListOfExpressions(n.initializers());
    boolean updaters = checkListOfExpressions(n.updaters());
    boolean body = checkBlock(n.getBody());
    if(condition && initializers && updaters && body)
      return n.getAST().newBlock();
    return null;
  }

  @Override public String description(ForStatement n) {
    return "remove :" + n;
  }
}
