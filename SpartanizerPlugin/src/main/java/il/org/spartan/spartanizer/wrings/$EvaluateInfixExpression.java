package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

import static il.org.spartan.spartanizer.ast.wizard.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

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
    return ¢.getOperator() == operator();
  }

  @Override public final ASTNode replacement(final InfixExpression x) {
    try {
      if(iz.validForEvaluation(x)){
      final String $ = opportunisticReplacement(x);
      if ($ != null && $.length() < (x + "").length())
        return x.getAST().newNumberLiteral($);
      }
      if(indexForLeftEvaluation(x)>1){
        String str = null;
        int index = indexForLeftEvaluation(x);
        InfixExpression cuttedExpression = subject.operands(extract.allOperands(x).subList(0,index)).to(operator());
        List<Expression> afterExpressionOperands = extract.allOperands(x).subList(index,extract.allOperands(x).size());
        if (iz.validForEvaluation(cuttedExpression)){
            str = opportunisticReplacement(cuttedExpression);
            if(str!=null) {
              return afterExpressionOperands.size() == 1
                  ? subject.pair(az.expression(x.getAST().newNumberLiteral(str)), afterExpressionOperands.get(0)).to(operator())
                  : subject.pair(az.expression(x.getAST().newNumberLiteral(str)), subject.operands(afterExpressionOperands).to(operator()))
                      .to(operator());
            }
        }
    }
    if(indexForRightEvaluation(x)>1 && operator()!=DIVIDE){
      String str = null;
      int index = indexForRightEvaluation(x);
        InfixExpression cuttedExpression = subject.operands(extract.allOperands(x).subList(extract.allOperands(x).size()-index,extract.allOperands(x).size())).to(operator());
        List<Expression> beforeExpressionOperands = extract.allOperands(x).subList(0,extract.allOperands(x).size()-index);
        if (iz.validForEvaluation(cuttedExpression)){
            str = opportunisticReplacement(cuttedExpression);
            if(str!=null){
                if(beforeExpressionOperands.size()==1){
                    return subject.pair(beforeExpressionOperands.get(0),az.expression(x.getAST().newNumberLiteral(str))).to(operator());
                }
                    return subject.pair(subject.operands(beforeExpressionOperands).to(operator()),az.expression(x.getAST().newNumberLiteral(str))).to(operator());
            }
        }

    }
    }catch (final Exception e) {
      Plugin.info(e);
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
  
  public static int indexForLeftEvaluation(final InfixExpression x) {
    final List<Expression> lst = extract.allOperands(x);
    int counter=0;
    for (final Expression ¢ : lst){
        if (!iz.number(¢)){
            if(counter>1)
                return counter;
            return 0;
        }
        counter++;
    }
    return 0;
}

public static int indexForRightEvaluation(final InfixExpression x) {
    final List<Expression> lst = extract.allOperands(x);
    int counter=0;
    for (int i= lst.size()-1 ; i>=0 ;i--){
        if (!iz.number(lst.get(i))){
            if(counter>1)
                return counter;
            return 0;
        }
        counter++;
    }
    return -1;
}

}