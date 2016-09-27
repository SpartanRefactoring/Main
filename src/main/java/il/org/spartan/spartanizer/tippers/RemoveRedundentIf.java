package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

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
    if(n==null)
      return null;
    if(!sideEffects.free(n.getExpression()))
      return null;
   if(n.getThenStatement()!=null && iz.block(n.getThenStatement())){
     List<Statement> lst = az.block(n.getThenStatement()).statements();
     for(Statement s :lst){
       if(az.expressionStatement(s)!=null){
         if(!sideEffects.free(az.expression(az.expressionStatement(s).getExpression())))
         return null;
     }
   }
  }
   return n.getAST().newBlock();
 }

  @Override public String description(IfStatement n) {
    return "remove :" + n;
  }
}
