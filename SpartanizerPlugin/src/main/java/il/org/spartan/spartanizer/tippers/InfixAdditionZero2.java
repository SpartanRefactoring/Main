package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to convert an expression such as
 *
 * <pre>
 * 0 + X = X
 * </pre>
 *
 * or
 *
 * <pre>
 * X + 0 = X
 * </pre>
 *
 * to
 *
 * <pre>
 * X
 * </i>
 * or
 * <pre>
 * X + 0 + Y
 * </pre>
 *
 * to
 *
 * <pre>
 * X + Y
 * </pre>
 *
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixAdditionZero2 extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  
  @SuppressWarnings("unused") @Override public ASTNode replacement(final InfixExpression ¢) {
    List<Expression> c = gather(¢, new ArrayList<Expression>());
    for(Expression a: c);
    
    Operator b = ¢.getOperator();
    List<Expression> allOperands = extract.allOperands(¢);
    List<Operator> allOperators = extract.allOperators(¢);
    List<Expression> ops = extract.allOperands(¢);
    
    ArrayList<Expression> ops2 = new ArrayList<Expression>();
    for(int i=0; i < ops.size(); i++){
      Expression ¢2 = ops.get(i);
      if(!iz.literal0(¢2)){
        ops2.add(ops.get(i));
      }      
    }    
    InfixExpression inexp = null;
    for(int i=0; i < ops2.size()-1; i++ ){
      if(inexp != null)
        inexp = subject.pair(inexp, ops2.get(i+1)).to(Operator.PLUS);
      else 
        inexp = subject.pair(ops2.get(i), ops2.get(i+1)).to(Operator.PLUS);
    }
    if(ops2.size() == 1)
      return ops2.get(0);
    return inexp;
  }

  @SuppressWarnings("static-method") private boolean containsZeroOperand(final InfixExpression ¢) {
    List<Expression> allOperands = extract.allOperands(¢);
    for(Expression opnd: allOperands)
      if(iz.literal0(opnd))
        return true;
    return false;
  }

  @SuppressWarnings("static-method") private boolean containsPlusOperator(final InfixExpression ¢) {
    List<Operator> allOperators = extract.allOperators(¢);
    for(Operator optor: allOperators)
      if(optor == Operator.PLUS)
        return true; 
    return false;
  }
  
//  public ASTNode replacement2(final InfixExpression ¢) {
//    List<Expression> ops = extract.allOperands(¢);
//    ArrayList<Expression> ops2 = new ArrayList<Expression>();
//    for(int i=0; i < ops.size(); i++){
//      Expression ¢2 = ops.get(i);
//      if(!iz.literal0(¢2)){
//        ops2.add(ops.get(i));
//      }      
//    }    
//    InfixExpression inexp = null;
//    for(int i=0; i < ops2.size()-1; i++ ){
//      if(inexp != null)
//        inexp = subject.pair(inexp, ops2.get(i+1)).to(Operator.PLUS);
//      else 
//        inexp = subject.pair(ops2.get(i), ops2.get(i+1)).to(Operator.PLUS);
//    }
//    if(ops2.size() == 1)
//      return ops2.get(0);
//    return inexp;
//  }
    
  @Override public boolean prerequisite(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && containsZeroOperand($) && containsPlusOperator($);
  }

  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  private static List<Expression> gather(final InfixExpression ¢) {
    return gather(¢, new ArrayList<Expression>());
  }

  private static List<Expression> gather(final InfixExpression x, final List<Expression> $) {
    if (x == null)
      return $;
    if (!in(x.getOperator(), PLUS, MINUS)) {
      $.add(x);
      return $;
    }
    gather(core(left(x)), $);
    gather(core(right(x)), $);
    if (x.hasExtendedOperands())
      gather(extendedOperands(x), $);
    return $;
  }

  private static List<Expression> gather(final List<Expression> xs, final List<Expression> $) {
    for (final Expression ¢ : xs)
      gather(¢, $);
    return $;
  }

  @Override public String description() {
    return "remove 0 in expressions like ";
  }

  @Override public String description(final InfixExpression ¢) {
    return description() + ¢;
  }

  @Override public TipperGroup tipperGroup() {
    return TipperGroup.Abbreviation;
  }
}
