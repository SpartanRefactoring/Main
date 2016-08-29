package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;


public class EvaluateAux {
  public enum Type {INT,LONG,DOUBLE,BAD}
  
  public static boolean isInt(Expression e){
    return e instanceof NumberLiteral && ((NumberLiteral) e).getToken().matches("[0-9]+");
  }
  
  public static boolean isDouble(Expression e){
    return e instanceof NumberLiteral && ((NumberLiteral) e).getToken().matches("[0-9]+\\.[0-9]?");
  }
  
  /* private static boolean isLong(Expression e){
  if(!(e instanceof NumberLiteral))
    return false;
  return ((NumberLiteral) e).getToken().matches("[0-9]+");
}*/ //TODO: Add supprot for long
  
 
 static boolean isCompitable(Expression e){
    
   return ((e instanceof NumberLiteral && isNumber(e))
       || ((e instanceof PrefixExpression) && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
       && (((PrefixExpression) e).getOperand() instanceof NumberLiteral)));
  } 
 
 static boolean isNumber(Expression e) {
   return isInt(e)||isDouble(e);
}

static EvaluateAux.Type getEvaluatedType(InfixExpression e){
    //boolean isLong = false;
    List<Expression> operands = extract.allOperands(e);
    for (final Expression ¢ : operands){
      if(!isCompitable(¢))
        return EvaluateAux.Type.BAD;
      if(EvaluateAux.isDouble(¢))
        return EvaluateAux.Type.DOUBLE;
    }
    return EvaluateAux.Type.INT;
  }
  
 static int extractInt(Expression e){
    return !(e instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) e).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }
  
 static double extractDouble(Expression e){
    return !(e instanceof PrefixExpression) ? Double.parseDouble(((NumberLiteral) e).getToken())
        : -1 * Double.parseDouble(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }
      
}
