package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.type.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;

/** A class containing auxiliary functions used by the arithmetic evaluation
 * wrings
 * @author Dor Ma'ayan
 * @since 2016 */
public interface EvaluateAux {
  // TODO: Dor, why do we need another type system?
  @Deprecated public enum Type {
    INT, LONG, DOUBLE, BAD;
    public static EvaluateAux.Type getEvaluatedType(final InfixExpression x) {
      final List<Expression> operands = extract.allOperands(x);
      boolean isLong = false;
      for (final Expression ¢ : operands) {
        if (!isCompatible(¢))
          return EvaluateAux.Type.BAD;
        if (type.isDouble(¢) || isMinusDouble(¢))
          return EvaluateAux.Type.DOUBLE;
        if (type.isLong(¢) || isMinusLong(¢))
          isLong = true;
      }
      return isLong ? EvaluateAux.Type.LONG : EvaluateAux.Type.INT;
    }

    public static EvaluateAux.Type getEvaluatedTypeForShift(final InfixExpression x) {
      final List<Expression> operands = extract.allOperands(x);
      final boolean isLong = isCompatible(first(operands)) && (type.isLong(first(operands)) || isMinusLong(first(operands)));
      for (final Expression ¢ : operands)
        if (!isCompatible(¢) || !isCompatible(¢) && (type.isDouble(¢) || isMinusDouble(¢)))
          return EvaluateAux.Type.BAD;
      return isLong ? EvaluateAux.Type.LONG : EvaluateAux.Type.INT;
    }
  }

  // TODO: Dor, I get Null Pointer Exceptions in this code.
  // Try to merge it with our services, so we can pinpoint the problem.
  // We have many iz.literal functions. Try to use them.
  static double extractDouble(final Expression x) {
    if (!type.isLong(x))
      return !(x instanceof PrefixExpression) ? Double.parseDouble(((NumberLiteral) x).getToken())
          : -1 * Double.parseDouble(((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken());
    final String token = ((NumberLiteral) x).getToken();
    if (!(x instanceof PrefixExpression))
      return Double.parseDouble(token.substring(0, token.length() - 1));
    final String negToken = ((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken();
    return -1 * Double.parseDouble(negToken.substring(0, negToken.length() - 1));
  }

  static int extractInt(final Expression x) {
    return !(x instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) x).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken());
  }

  static long extractLong(final Expression x) {
    final String token = ((NumberLiteral) x).getToken();
    if (type.isInt(x) || isMinusInt(x))
      return Long.parseLong(token);
    if (!(x instanceof PrefixExpression))
      return Long.parseLong(token.substring(0, token.length() - 1));
    // TODO: Dor, we have something that says whether a literal is negative.
    // You may want to search for '-' or "-" in the entire code base.
    final String negToken = ((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken();
    return -1 * Long.parseLong(negToken.substring(0, negToken.length() - 1));
  }

  static boolean isCompatible(final Expression x) {
    return x instanceof NumberLiteral && isNumber(x) || x instanceof PrefixExpression
        && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS && ((PrefixExpression) x).getOperand() instanceof NumberLiteral;
  }

  static boolean isMinusDouble(final Expression x) {
    return iz.prefixExpression(x) && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && type.isDouble(((PrefixExpression) x).getOperand());
  }


  static boolean isMinusInt(final Expression x) {
    return iz.prefixExpression(x) && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && type.isInt(((PrefixExpression) x).getOperand());
  }

  static boolean isMinusLong(final Expression x) {
    return x instanceof PrefixExpression && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && type.isLong(((PrefixExpression) x).getOperand());
  }

  static boolean isNumber(final Expression x) {
    return type.isInt(x) || type.isDouble(x) || isMinusDouble(x) || isMinusInt(x) || type.isLong(x) || isMinusLong(x);
  }
}
