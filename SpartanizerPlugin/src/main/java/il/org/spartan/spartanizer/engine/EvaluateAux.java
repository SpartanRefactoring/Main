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
  static double extractDouble(final Expression x) {
    if (!iz.longType(x))
      return !iz.prefixExpression(x) ? Double.parseDouble(az.numberLiteral(x).getToken())
          : -1 * Double.parseDouble(az.numberLiteral(az.prefixExpression(x).getOperand()).getToken());
    final String token = az.numberLiteral(x).getToken();
    if (!iz.prefixExpression(x))
      return Double.parseDouble(token.substring(0, token.length() - 1));
    final String negToken = az.numberLiteral(az.prefixExpression(x).getOperand()).getToken();
    return -1 * Double.parseDouble(negToken.substring(0, negToken.length() - 1));
  }

  static int extractInt(final Expression x) {
    return !iz.prefixExpression(x) ? Integer.parseInt(az.numberLiteral(x).getToken())
        : -1 * Integer.parseInt(az.numberLiteral(az.prefixExpression(x).getOperand()).getToken());
  }

  static long extractLong(final Expression x) {
    final String token = az.numberLiteral(x).getToken();
    if (iz.intType(x))
      return Long.parseLong(token);
    if (!iz.prefixExpression(x))
      return Long.parseLong(token.substring(0, token.length() - 1));
    final String negToken = az.numberLiteral((az.prefixExpression(x).getOperand())).getToken();
    return -1 * Long.parseLong(negToken.substring(0, negToken.length() - 1));
  }

  static type getEvaluatedType(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
    for (final Expression ¢ : operands) {
      if (!isCompatible(¢))
        return null;
      if (iz.doubleType(¢))
        return DOUBLE;
      if (iz.longType(¢))
        isLong = true;
    }
    return isLong ? LONG : INT;
  }

  static type getEvaluatedTypeForShift(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
    isLong = isCompatible(first(operands)) && (iz.longType(first(operands)));
    for (final Expression ¢ : operands)
      if (!isCompatible(¢) || !isCompatible(¢) && (iz.doubleType(¢)))
        return null;
    return isLong ? LONG : INT;
  }

  static boolean isCompatible(final Expression x) {
    //TODO: Dor, i
    return iz.numberLiteral(x) && isNumber(x) || iz.prefixExpression(x)
        && (az.prefixExpression(x).getOperator() == PrefixExpression.Operator.MINUS && iz.numberLiteral(az.prefixExpression(x).getOperand()));
  }

  static boolean isNumber(final Expression x) {
    return type.isInt(x) || type.isDouble(x) || type.isLong(x);
  }
}
