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
    
//    List<Expression> c = gather(¢);
    List<Expression> c = gather(¢, new ArrayList<Expression>());
    System.out.println("c.size(): " + c.size());
    System.out.println("core(¢): " + core(¢));
    System.out.println("left(¢): " + left(¢));
    System.out.println("right(¢): " + right(¢));
    System.out.println("right(¢): " + right(¢));
    for(Expression a: c)
      System.out.println("gather(¢): " + a);
    
    Operator b = ¢.getOperator();
    if(Operator.PLUS == b) 
      System.out.println("ok");
    List<Expression> allOperands = extract.allOperands(¢);
    List<Operator> allOperators = extract.allOperators(¢);
//    containsPlusOperator(¢);
//    containsZeroOperand(¢);
    Object a = replacement(allOperands);
    return replacement2(¢);
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
  
  @SuppressWarnings("static-method") public ASTNode replacement2(final InfixExpression ¢) {
//    System.out.println("left(¢): " + left(¢));
//    System.out.println("right(¢): " + right(¢));
    System.out.println(extract.allOperands(¢));
    List<Expression> ops = extract.allOperands(¢);
    ArrayList<Expression> ops2 = new ArrayList<Expression>();
    for(int i=0; i < ops.size(); i++){
      System.out.println(i);
      System.out.println(ops2);
      Expression ¢2 = ops.get(i);
      if(!iz.literal0(¢2)){
        ops2.add(ops.get(i));
      }      
    }
    
    System.out.println("ops2: " + ops);
    
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
  
  
  @Override public boolean prerequisite(final InfixExpression $) {
    System.out.println("$: " + $);
    System.out.println("iz.infixPlus($): " + iz.infixPlus($));
    System.out.println("containsZeroOperand($): " + containsZeroOperand($));
    System.out.println("containsPlusOperator($): " + containsPlusOperator($));
    return $ != null && iz.infixPlus($) && containsZeroOperand($) &&
        containsPlusOperator($);// && IsSimpleMultiplication(left($)) && IsSimpleMultiplication(right($));
  }
  /**
   * @param allOperands
   * @return
   */
  @SuppressWarnings("static-method") private Object replacement(List<Expression> allOperands) {
    return null;
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
    System.out.println("x:" + x);
    if (x == null)
      return $;
    System.out.println("x.getOperator().PLUS: " + x.getOperator().PLUS);
    System.out.println("in(x.getOperator(), PLUS, MINUS): " + in(x.getOperator(), Operator.PLUS, Operator.MINUS));
    System.out.println("in(x.getOperator(), PLUS): " + in(x.getOperator(), Operator.PLUS));
    if (!in(x.getOperator(), PLUS, MINUS)) {
      $.add(x);
      return $;
    }
    gather(core(left(x)), $);
    gather(core(right(x)), $);
    if (x.hasExtendedOperands())
      gather(extendedOperands(x), $);
    System.out.println(" --->> $: " + $);
    return $;
  }

  private static List<Expression> gather(final List<Expression> xs, final List<Expression> $) {
    for (final Expression ¢ : xs)
      gather(¢, $);
    return $;
  }

  @Override public String description() {
    return "remove 0 in X + 0 expressions from ";
  }

  @Override public String description(final InfixExpression ¢) {
    return description() + ¢;
  }

//  @Override public Tip tip(final InfixExpression x, final ExclusionManager exclude) {
//    System.out.println("exclude: " + exclude);
//    System.out.println("x: " + x);
//    final List<Expression> es = gather(x);
//    System.out.println("es.size(): " + es.size());
//    for(Expression exp: es)
//      System.out.println(exp);
//    if (es.size() < 2)
//      return null;
//    final int n = minus.level(es);
//    if (n == 0 || n == 1 && minus.level(first(es)) == 1)
//      return null;
//    if (exclude != null)
//      exclude.exclude(x);
//    return new Tip(description(x), x, this.getClass()) {
//      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
//        final Expression first = n % 2 == 0 ? null : es.get(0);
//        for (final Expression ¢ : es)
//          if (¢ != first && minus.level(¢) > 0)
//            r.replace(¢, plant(duplicate.of(minus.peel(¢))).into(¢.getParent()), g);
//        if (first != null)
//          r.replace(first, plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
//      }
//    };
//  }

  @Override public TipperGroup tipperGroup() {
    return TipperGroup.Abbreviation;
  }
}
