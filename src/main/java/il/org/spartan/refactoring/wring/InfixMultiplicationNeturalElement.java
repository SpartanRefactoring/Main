package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>1*X</code> by <code>X</code> 
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixMultiplicationNeturalElement extends ReplaceCurrentNode<InfixExpression> {
  @Override String description(final InfixExpression e) {
    return "Remove all multiplications by 1 from " + e;
  }
  @Override ASTNode replacement(InfixExpression e) {
    return (e.getOperator() != TIMES) ? null : replacement(extract.allOperands(e));
  }
  private static ASTNode replacement(final List<Expression> es) {
    List<Expression> $ = new ArrayList<>();
    for (Expression ¢ : es)
      if (!isLiteralOne(¢))
        $.add(¢);
    return $.size() == es.size() ? null : $.size() == 0 ? duplicate(es.get(0)) : $.size() == 1 ? duplicate($.get(0)) : subject.operands($).to(TIMES);
  }
  private static boolean isLiteralOne(Expression ¢) {
    return isLiteralOne(asNumberLiteral(¢));
  }
  private static boolean isLiteralOne(NumberLiteral ¢) {
    return ¢ != null && isLiteralOne(¢.getToken());
  }
  private static boolean isLiteralOne(String ¢) {
    try {
      return Integer.parseInt(¢) == 1;
    } catch (@SuppressWarnings("unused") NumberFormatException __) {
      return false;
    }
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}