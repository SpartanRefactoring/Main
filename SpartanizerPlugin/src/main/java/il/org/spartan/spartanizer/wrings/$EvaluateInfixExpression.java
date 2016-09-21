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

  @Override public final String description(final InfixExpression ¢) {
    return description() + ":" + ¢;
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

  abstract double evaluateDouble(List<Expression> xs) throws Exception;

  abstract int evaluateInt(List<Expression> xs) throws Exception;

  abstract long evaluateLong(List<Expression> xs) throws Exception;

  abstract String operation();

  abstract Operator operator();

  private String opportunisticReplacement(final InfixExpression ¢) throws Exception {
    return type.of(¢) == INT ? Integer.toString(evaluateInt(extract.allOperands(¢)))
        : type.of(¢) == DOUBLE ? Double.toString(evaluateDouble(extract.allOperands(¢)))
            : type.of(¢) == LONG ? Long.toString(evaluateLong(extract.allOperands(¢))) + "L" : null;
  }
}