package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
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
    final List<Expression> c = gather(¢, new ArrayList<Expression>());
    final Operator b = ¢.getOperator();
    final List<Expression> allOperands = extract.allOperands(¢);
    final List<Operator> allOperators = extract.allOperators(¢);
    final List<Expression> ops = extract.allOperands(¢);
    final ArrayList<Expression> ops2 = new ArrayList<>();
    for (int i = 0; i < ops.size(); i++) {
      final Expression ¢2 = ops.get(i);
//      System.out.println(iz.stringLiteral(ops.get(i-1)));
//      if(iz.stringLiteral(¢2))
//        System.out.println("string literal: " + ¢2);
//      if(!iz.stringLiteral(¢2)){
//        System.out.println("string literal: " + ¢2);
      if (!iz.literal0(¢2)) {
        System.out.println(¢2);
        ops2.add(ops.get(i));
      }
      else // if(iz.literal0(¢2))
          if((i+1) < ops.size())
            if(iz.stringLiteral(ops.get(i+1))){
              System.out.println("string after");
              ops2.add(¢2);
              }
          else 
          if(i>0){
            System.out.println("string before");
            if(iz.stringLiteral(ops.get(i-1))){
              System.out.println("string before");
              ops2.add(¢2);
            }
          }
            
//        else if(iz.stringLiteral(¢2))
//          if((i+1) < ops.size())
//            if(iz.literal0(ops.get(i+1)))
//              ops2.add(ops.get(i));
//        else if(iz.stringLiteral(¢2))
//          if((i+1) < ops.size())
//             if(iz.literal0(ops.get(i+1)))
//                  ops2.add(ops.get(i));
//          else
//            ops2.add(subject.append(subject.pair(¢2,ops.get(i+1)).to(Operator.PLUS)));
//        }
    }
    InfixExpression inexp = null;
    for (int i = 0; i < ops2.size() - 1; i++)
      if (inexp != null)
        inexp = subject.pair(inexp, ops2.get(i + 1)).to(Operator.PLUS);
      else
        inexp = subject.pair(ops2.get(i), ops2.get(i + 1)).to(Operator.PLUS);
    if (ops2.size() == 1)
      return ops2.get(0);
    return inexp;
  }

  @SuppressWarnings("static-method") private boolean containsZeroOperand(final InfixExpression ¢) {
    final List<Expression> allOperands = extract.allOperands(¢);
    for (final Expression opnd : allOperands)
      if (iz.literal0(opnd))
        return true;
    return false;
  }

  @SuppressWarnings("static-method") private boolean containsPlusOperator(final InfixExpression ¢) {
    final List<Operator> allOperators = extract.allOperators(¢);
    for (final Operator optor : allOperators)
      if (optor == Operator.PLUS)
        return true;
    return false;
  }

  // public ASTNode replacement2(final InfixExpression ¢) {
  // List<Expression> ops = extract.allOperands(¢);
  // ArrayList<Expression> ops2 = new ArrayList<Expression>();
  // for(int i=0; i < ops.size(); i++){
  // Expression ¢2 = ops.get(i);
  // if(!iz.literal0(¢2)){
  // ops2.add(ops.get(i));
  // }
  // }
  // InfixExpression inexp = null;
  // for(int i=0; i < ops2.size()-1; i++ ){
  // if(inexp != null)
  // inexp = subject.pair(inexp, ops2.get(i+1)).to(Operator.PLUS);
  // else
  // inexp = subject.pair(ops2.get(i), ops2.get(i+1)).to(Operator.PLUS);
  // }
  // if(ops2.size() == 1)
  // return ops2.get(0);
  // return inexp;
  // }
  @Override public boolean prerequisite(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && containsZeroOperand($) && containsPlusOperator($);
  }

  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
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
