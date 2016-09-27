package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

public class RemoveRedundent {
  public static boolean checkVariableDecleration(VariableDeclarationStatement s ){    
    List<VariableDeclarationFragment> lst = s.fragments();
    for(VariableDeclarationFragment ¢ : lst)
      if (¢.getInitializer() != null && !sideEffects.free(¢.getInitializer()))
        return false;
    return true;
  }
  public static boolean checkBlock(ASTNode n){
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
  
  public static boolean checkListOfExpressions(List<Expression> lst){
    for(Expression ¢: lst)
      if(!sideEffects.free(¢))
        return false;
    return true;
  }
}
