package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Common strategy of all evaluators$EvaluateExpression
 * @author Yossi Gil
 * @year 2016 */
abstract class $EvaluateInfixExpression extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  @Override public final String description() {
    return "Evaluate " + operation();
  }

  @Override public final String description(final InfixExpression x) {
    return description() + ":" + x;
  }

  @Override public final boolean prerequisite(final InfixExpression ¢) {
    return ¢.getOperator() == operator() && iz.validForEvaluation(¢);
  }

  @Override public final ASTNode replacement(final InfixExpression ¢) {
    try {
      final String $ = opportunisticReplacement(¢);
      if ($ != null && $.length() < (¢ + "").length())
        return ¢.getAST().newNumberLiteral($);
    } catch (final Exception x) {
      Plugin.info(x);
    }
    return null;
  }

  @Override public final WringGroup wringGroup() {
    return super.wringGroup();
  }

  abstract double evaluateDouble(List<Expression> es) throws Exception;

  abstract int evaluateInt(List<Expression> es) throws Exception;

  abstract long evaluateLong(List<Expression> es) throws Exception;

  abstract String operation();

  abstract Operator operator();

  private final String opportunisticReplacement(final InfixExpression x) throws Exception {
    if (type.of(x) == INT)
      return Integer.toString(evaluateInt(extract.allOperands(x)));
    if (type.of(x) == DOUBLE)
      return Double.toString(evaluateDouble(extract.allOperands(x)));
    if (type.of(x) == LONG)
      return Long.toString(evaluateLong(extract.allOperands(x))) + "L";
    return null;
  }
}