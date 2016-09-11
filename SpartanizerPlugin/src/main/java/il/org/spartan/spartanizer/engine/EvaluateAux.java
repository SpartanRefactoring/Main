package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

// TODO: Niv, add header.
public class EvaluateAux {
  public static double extractDouble(final Expression x) {
    if (!isLong(x))
      return !(x instanceof PrefixExpression) ? Double.parseDouble(((NumberLiteral) x).getToken())
          : -1 * Double.parseDouble(((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken());
    final String token = ((NumberLiteral) x).getToken();
    if (!(x instanceof PrefixExpression))
      return Double.parseDouble(token.substring(0, token.length() - 1));
    final String negToken = ((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken();
    return -1 * Double.parseDouble(negToken.substring(0, negToken.length() - 1));
  }

  public static int extractInt(final Expression x) {
    return !(x instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) x).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken());
  }

  public static long extractLong(final Expression x) {
    final String token = ((NumberLiteral) x).getToken();
    if (isInt(x) || isMinusInt(x))
      return Long.parseLong(token);
    if (!(x instanceof PrefixExpression))
      return Long.parseLong(token.substring(0, token.length() - 1));
    final String negToken = ((NumberLiteral) ((PrefixExpression) x).getOperand()).getToken();
    return -1 * Long.parseLong(negToken.substring(0, negToken.length() - 1));
  }

  public static EvaluateAux.Type getEvaluatedType(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
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

  public static EvaluateAux.Type getEvaluatedTypeForShift(final InfixExpression x) {
    boolean isLong = false;
    final List<Expression> operands = extract.allOperands(x);
    // TODO: Niv, no spelling errors please
    isLong = isCompitable(first(operands)) && (isLong(first(operands)) || isMinusLong(first(operands)));
    for (final Expression ¢ : operands)
      if (!isCompitable(¢) || !isCompitable(¢) && (EvaluateAux.isDouble(¢) || isMinusDouble(¢)))
        return EvaluateAux.Type.BAD;
    return isLong ? EvaluateAux.Type.LONG : EvaluateAux.Type.INT;
  }

  public static boolean isCompitable(final Expression x) {
    return x instanceof NumberLiteral && isNumber(x) || x instanceof PrefixExpression
        && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS && ((PrefixExpression) x).getOperand() instanceof NumberLiteral;
  }

  public static boolean isDouble(final Expression x) {
    return type.get(x) == type.Primitive.Certain.DOUBLE;
  }

  public static boolean isInt(final Expression x) {
    return type.get(x) == type.Primitive.Certain.INT;
  }

  public static boolean isLong(final Expression x) {
    return type.get(x) == type.Primitive.Certain.LONG;
  }

  public static boolean isMinusDouble(final Expression x) {
    return iz.prefixExpression(x) && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && isDouble(((PrefixExpression) x).getOperand());
  }

  public static boolean isMinusInt(final Expression x) {
    return iz.prefixExpression(x) && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && isInt(((PrefixExpression) x).getOperand());
  }

  public static boolean isMinusLong(final Expression x) {
    return x instanceof PrefixExpression && ((PrefixExpression) x).getOperator() == PrefixExpression.Operator.MINUS
        && isLong(((PrefixExpression) x).getOperand());
  }

  public static boolean isNumber(final Expression x) {
    return isInt(x) || isDouble(x) || isMinusDouble(x) || isMinusInt(x) || isLong(x) || isMinusLong(x);
  }

  public enum Type {
    INT, LONG, DOUBLE, BAD
  }
}
