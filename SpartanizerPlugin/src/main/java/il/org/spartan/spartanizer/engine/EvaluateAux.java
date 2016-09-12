package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;

/** A class containing auxiliary functions used by the arithmetic evaluation
 * wrings
 * @author Dor Ma'ayan
 * @since 2016 */

public interface EvaluateAux {
  // TODO: Dor, I get Null Pointer Exceptions in this code.
  // Try to merge it with our services, so we can pinpoint the problem.
  // We have many iz.literal functions. Try to use them.
      public static double extractDouble(final Expression x) {
        if (!isLong(x))
          return !iz.prefixExpression(x) ? Double.parseDouble(az.numberLiteral(x).getToken())
              : -1 * Double.parseDouble(az.numberLiteral(az.prefixExpression(x).getOperand()).getToken());
        final String token = az.numberLiteral(x).getToken();
        if (!iz.prefixExpression(x))
      return Double.parseDouble(token.substring(0, token.length() - 1));
    final String negToken = az.numberLiteral(az.prefixExpression(x).getOperand()).getToken();
    return -1 * Double.parseDouble(negToken.substring(0, negToken.length() - 1));
  }

  public static int extractInt(final Expression x) {
    return !iz.prefixExpression(x) ? Integer.parseInt(az.numberLiteral(x).getToken())
        : -1 * Integer.parseInt(az.numberLiteral(az.prefixExpression(x).getOperand()).getToken());
  }

//TODO: Dor, we have something that says whether a literal is negative.
// You may want to search for '-' or "-" in the entire code base.  
public static long extractLong(final Expression x) {
    final String token = az.numberLiteral(x).getToken();
    if (isInt(x) || isMinusInt(x))
      return Long.parseLong(token);
    if (!(x instanceof PrefixExpression))
      return Long.parseLong(token.substring(0, token.length() - 1));
    final String negToken = az.numberLiteral((az.prefixExpression(x).getOperand())).getToken();
    return -1 * Long.parseLong(negToken.substring(0, negToken.length() - 1));
  }

  public static type getEvaluatedType(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
    for (final Expression ¢ : operands) {
      if (!isCompatible(¢))
        return null;
      if (EvaluateAux.isDouble(¢) || isMinusDouble(¢))
        return DOUBLE;
      if (isLong(¢) || isMinusLong(¢))
        isLong = true;
    }
    return isLong ? LONG : INT;
  }

  public static type getEvaluatedTypeForShift(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
    isLong = isCompatible(first(operands)) && (isLong(first(operands)) || isMinusLong(first(operands)));
    for (final Expression ¢ : operands)
      if (!isCompatible(¢) || !isCompatible(¢) && (EvaluateAux.isDouble(¢) || isMinusDouble(¢)))
        return null;
    return isLong ? LONG : INT;
  }

  public static boolean isCompatible(final Expression x) {
    return x instanceof NumberLiteral && isNumber(x) || iz.prefixExpression(x)
        && (az.prefixExpression(x).getOperator() == PrefixExpression.Operator.MINUS && iz.numericLiteral(az.prefixExpression(x).getOperand()));
  }

  public static boolean isDouble(final Expression x) {
    return type.get(x) == DOUBLE;
  }

  public static boolean isInt(final Expression x) {
    return type.get(x) == INT;
  }

  public static boolean isLong(final Expression x) {
    return type.get(x) == LONG;
  }

  public static boolean isMinusDouble(final Expression x) {
    return iz.prefixExpression(x) && az.prefixExpression(x).getOperator() == PrefixExpression.Operator.MINUS
        && isDouble(az.prefixExpression(x).getOperand());
  }

  public static boolean isMinusInt(final Expression x) {
    return iz.prefixExpression(x) && az.prefixExpression(x).getOperator() == PrefixExpression.Operator.MINUS
        && isInt(az.prefixExpression(x).getOperand());
  }

  public static boolean isMinusLong(final Expression x) {
    return  iz.prefixExpression(x) && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && isLong(az.prefixExpression(x).getOperand());
  }


  static boolean isNumber(final Expression x) {
    return type.isInt(x) || type.isDouble(x) || isMinusDouble(x) || isMinusInt(x) || type.isLong(x) || isMinusLong(x);
  }

}
