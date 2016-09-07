package il.org.spartan.spartanizer.engine;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

public class EvaluateAux {
  public static double extractDouble(final Expression e) {
    if (!isLong(e))
      return !(e instanceof PrefixExpression) ? Double.parseDouble(((NumberLiteral) e).getToken())
          : -1 * Double.parseDouble(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
    final String token = ((NumberLiteral) e).getToken();
    if (!(e instanceof PrefixExpression))
      return Double.parseDouble(token.substring(0, token.length() - 1));
    final String negToken = ((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken();
    return -1 * Double.parseDouble(negToken.substring(0, negToken.length() - 1));
  }

  public static int extractInt(final Expression e) {
    return !(e instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) e).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }

  public static long extractLong(final Expression e) {
    final String token = ((NumberLiteral) e).getToken();
    if (isInt(e) || isMinusInt(e))
      return Long.parseLong(token);
    if (!(e instanceof PrefixExpression))
      return Long.parseLong(token.substring(0, token.length() - 1));
    final String negToken = ((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken();
    return -1 * Long.parseLong(negToken.substring(0, negToken.length() - 1));
  }

  public static EvaluateAux.Type getEvaluatedType(final InfixExpression e) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(e);
    for (final Expression ¢ : operands) {
      if (!isCompitable(¢))
        return EvaluateAux.Type.BAD;
      if (EvaluateAux.isDouble(¢) || isMinusDouble(¢))
        return EvaluateAux.Type.DOUBLE;
      if (isLong(¢) || isMinusLong(¢))
        isLong = true;
    }
    return isLong ? EvaluateAux.Type.LONG : EvaluateAux.Type.INT;
  }

  public static EvaluateAux.Type getEvaluatedTypeForShift(final InfixExpression e) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(e);
    isLong = isCompitable(operands.get(0)) && (isLong(operands.get(0)) || isMinusLong(operands.get(0)));
    for (final Expression ¢ : operands)
      if (!isCompitable(¢) || !isCompitable(¢) && (EvaluateAux.isDouble(¢) || isMinusDouble(¢)))
        return EvaluateAux.Type.BAD;
    return isLong ? EvaluateAux.Type.LONG : EvaluateAux.Type.INT;
  }

  public static boolean isCompitable(final Expression e) {
    return e instanceof NumberLiteral && isNumber(e) || e instanceof PrefixExpression
        && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS && ((PrefixExpression) e).getOperand() instanceof NumberLiteral;
  }

  public static boolean isDouble(final Expression e) {
    return PrudentType.prudent(e) == PrudentType.DOUBLE;
  }

  public static boolean isInt(final Expression e) {
    return PrudentType.prudent(e) == PrudentType.INT;
  }

  public static boolean isLong(final Expression e) {
    return PrudentType.prudent(e) == PrudentType.LONG;
  }

  public static boolean isMinusDouble(final Expression e) {
    return iz.prefixExpression(e) && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
        && isDouble(((PrefixExpression) e).getOperand());
  }

  public static boolean isMinusInt(final Expression e) {
    return iz.prefixExpression(e) && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
        && isInt(((PrefixExpression) e).getOperand());
  }

  public static boolean isMinusLong(final Expression e) {
    return e instanceof PrefixExpression && ((PrefixExpression) e).getOperator() == PrefixExpression.Operator.MINUS
        && isLong(((PrefixExpression) e).getOperand());
  }

  public static boolean isNumber(final Expression e) {
    return isInt(e) || isDouble(e) || isMinusDouble(e) || isMinusInt(e) || isLong(e) || isMinusLong(e);
  }

  public enum Type {
    INT, LONG, DOUBLE, BAD
  }
}
