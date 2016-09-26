package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace X == null ? null : X.Y with X?.Y <br>
 * replace X != null ? X.Y : null with X?.Y <br>
 * replace null == X ? null : X.Y with X?.Y <br>
 * replace null != X ? X.Y : null with X?.Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class TernaryNullConditional extends ReplaceCurrentNode<ConditionalExpression> implements Kind.CommnoFactoring {
  @Override public ASTNode replacement(ConditionalExpression x) {
    if (!iz.comparison(az.infixExpression(step.expression(x))))
      return null;
    InfixExpression condition = az.comparison((step.expression(x)));
    Expression left = step.left(condition);
    Expression right = step.right(condition);
    ASTNode then = step.then(x);
    ASTNode elze = step.elze(x);
    if(iz.nullLiteral(left) && iz.memberRef(right)){
      
    }
    if(iz.memberRef(left) && iz.nullLiteral(right)){
      
    }
//    return null;
//    
////    MemberRef m = ;
    return step.operator(condition) == EQUALS ? replacement(left, right, step.elze(x))
        : step.operator(condition) == NOT_EQUALS ? replacement(left, right, step.then(x)) : null;
  }

  private static ASTNode replacement(Expression left, Expression right, Expression elze) {
    if ((!iz.nullLiteral(left) && iz.nullLiteral(right) && wizard.same(left, elze)))
      Counter.count(TernaryNullConditional.class);
    if (iz.nullLiteral(left) && !iz.nullLiteral(right) && wizard.same(right, elze))
      Counter.count(TernaryNullConditional.class);
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") ConditionalExpression __) {
    return "replace null coallescing ternary with ??";
  }
}
