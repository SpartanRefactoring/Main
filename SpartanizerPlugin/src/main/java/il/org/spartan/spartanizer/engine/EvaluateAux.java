package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** A class containing auxiliary functions used by the arithmetic evaluation
 * wrings
 * @author Dor Ma'ayan
 * @since 2016 */
public interface EvaluateAux {
  static double extractDouble(final Expression x) {
    // TODO: Assert something?
    if (!iz.longType(x))
      return !iz.prefixExpression(x) ? Double.parseDouble(az.numberLiteral(x).getToken())
          : -1 * Double.parseDouble(az.numberLiteral(az.prefixExpression(x).getOperand()).getToken());
    // TODO: DOR I am confused, are you or aren't you sure that this is a
    // numberLiteral at this point, if it is it cannot be PrefixExpession. If
    // not, how can you extract the token? I suggest you do extract method, to
    // deal with the case you know it is a number literal. If you do that, you
    // will see the error I think
    final String token = az.numberLiteral(x).getToken();
    if (!iz.prefixExpression(x))
      return Double.parseDouble(token.substring(0, token.length() - 1));
    final String negToken = az.numberLiteral(az.prefixExpression(x).getOperand()).getToken();
    return -1 * Double.parseDouble(negToken.substring(0, negToken.length() - 1));
  }

  static int extractInt(final Expression ¢) {
    // Again, extract method to deal with the case it is a literal, will clarify
    // the logic. Would it make sense to add here something like `assert
    // type.get(x) == LONG)` or something similar
    return !iz.prefixExpression(¢) ? Integer.parseInt(az.numberLiteral(¢).getToken())
        : -1 * Integer.parseInt(az.numberLiteral(az.prefixExpression(¢).getOperand()).getToken());
  }

  static long extractLong(final Expression x) {
    // Again, extract method to deal with the case it is a literal, will clarify
    // the logic.
    final String token = az.numberLiteral(x).getToken();
    if (iz.intType(x))
      return Long.parseLong(token);
    if (!iz.prefixExpression(x))
      return Long.parseLong(token.substring(0, token.length() - 1));
    final String negToken = az.numberLiteral(az.prefixExpression(x).getOperand()).getToken();
    return -1 * Long.parseLong(negToken.substring(0, negToken.length() - 1));
  }

  // TODO: This function seems redundant. You should do `type.get(x)` or else
  // you would be duplicating the complicated logic.
  static type getEvaluatedType(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
    // Confused by the logic, when do you return null, and when do you return
    // DOUBLE? and isn't this part of `type`?
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
    isLong = isCompatible(first(operands)) && iz.longType(first(operands));
    for (final Expression ¢ : operands)
      if (!isCompatible(¢) || !isCompatible(¢) && iz.doubleType(¢))
        return null;
    return isLong ? LONG : INT;
  }

  static boolean isCompatible(final Expression ¢) {
    // TODO: Dor, one of the following two check may re redundant
    return iz.numberLiteral(¢) && isNumber(¢) || iz.prefixExpression(¢) && az.prefixExpression(¢).getOperator() == PrefixExpression.Operator.MINUS
        && iz.numberLiteral(az.prefixExpression(¢).getOperand());
  }

  static boolean isNumber(final Expression ¢) {
    return type.isInt(¢) || type.isDouble(¢) || type.isLong(¢);
  }
}
