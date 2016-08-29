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
  
  public static boolean isLong(Expression e){
    return e instanceof NumberLiteral && ((NumberLiteral) e).getToken().matches("[0-9]+L");
  }
  
  public static boolean isMinusDouble(Expression e){
    return (e instanceof PrefixExpression) && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
        && (((PrefixExpression) e).getOperand() instanceof NumberLiteral) 
        && ((NumberLiteral)((PrefixExpression) e).getOperand()).getToken().matches("[0-9]+\\.[0-9]?");
  }
  
  public static boolean isMinusInt(Expression e){
    return (e instanceof PrefixExpression) && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
        && (((PrefixExpression) e).getOperand() instanceof NumberLiteral) 
        && ((NumberLiteral)((PrefixExpression) e).getOperand()).getToken().matches("[0-9]+");
  }
  
  public static boolean isMinusLong(Expression e){
    return (e instanceof PrefixExpression) && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
        && (((PrefixExpression) e).getOperand() instanceof NumberLiteral) 
        && ((NumberLiteral)((PrefixExpression) e).getOperand()).getToken().matches("[0-9]+L");
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
   return isInt(e)||isDouble(e)||isMinusDouble(e)||isMinusInt(e) || isLong(e) || isMinusLong(e);
}

static EvaluateAux.Type getEvaluatedType(InfixExpression e){
    boolean isLong = false;
    List<Expression> operands = extract.allOperands(e);
    for (final Expression ¢ : operands){
      if(!isCompitable(¢))
        return EvaluateAux.Type.BAD;
      if(EvaluateAux.isDouble(¢) || isMinusDouble(¢) )
        return EvaluateAux.Type.DOUBLE;
      if(isLong(¢) || isMinusLong(¢))
        isLong= true;
    }    
    return isLong ? EvaluateAux.Type.LONG :EvaluateAux.Type.INT;
  }
  
 static int extractInt(Expression e){
    return !(e instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) e).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }
  
 static double extractDouble(Expression e){
   if(isLong(e)){
     String token = ((NumberLiteral) e).getToken();
     if(!(e instanceof PrefixExpression))
       return Double.parseDouble(token.substring(0, token.length()-1));
     String negToken = ((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken();
     return -1 * Double.parseDouble(negToken.substring(0, negToken.length()-1));
   }
   return !(e instanceof PrefixExpression) ? Double.parseDouble(((NumberLiteral) e).getToken())
        : -1 * Double.parseDouble(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }
 
 static long extractLong(Expression e){
   String token = ((NumberLiteral) e).getToken();
   if(isInt(e)|| isMinusInt(e))
     return Long.parseLong(token);
   if( !(e instanceof PrefixExpression))  
     return Long.parseLong(token.substring(0, token.length()-1));
   String negToken = ((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken();
   return -1 * Long.parseLong(negToken.substring(0, negToken.length()-1));
 }
      
}
