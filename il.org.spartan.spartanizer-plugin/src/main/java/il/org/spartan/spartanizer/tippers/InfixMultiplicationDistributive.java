package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Apply the distributive rule to multiplication: {@code
* a*b + a*c
 * } to <code>
* <b>a * (b + c)</b>
 * </code> .
 * @author Matteo Orru'
 * @since 2016 */
public final class InfixMultiplicationDistributive extends ReplaceCurrentNode<InfixExpression>
    //
    implements Category.Theory.Arithmetics.Symbolic, Category.CommonFactorOut {
  private static final long serialVersionUID = -0x381154E4E51D0081L;

  private static boolean IsSimpleMultiplication(final Expression $) {
    return !iz.simpleName($) && ((InfixExpression) $).getOperator() == TIMES;
  }
  @Override public String description() {
    return "a*b + a*c => a * (b + c)";
  }
  @Override public String description(final InfixExpression ¢) {
    return "Apply the distributive rule to " + ¢;
  }
  @Override public boolean prerequisite(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && IsSimpleMultiplication(left($)) && IsSimpleMultiplication(right($));
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != PLUS ? null : replacement(extract.allOperands(¢));
  }
  private void addCommon(final Expression op, final Collection<Expression> common) {
    addNewInList(op, common);
  }
  private void addDifferent(final Expression op, final Collection<Expression> different) {
    addNewInList(op, different);
  }
  private void addNewInList(final Expression item, final Collection<Expression> xs) {
    if (!isIn(item, xs))
      xs.add(item);
  }
  @SuppressWarnings("static-method") private boolean isIn(final Expression op, final Collection<Expression> allOperands) {
    return allOperands.stream().anyMatch(λ -> wizard.eq(op, λ));
  }
  private ASTNode replacement(final InfixExpression e1, final InfixExpression e2) {
    assert e1 != null;
    assert e2 != null;
    final List<Expression> es1 = extract.allOperands(e1);
    assert es1 != null;
    final List<Expression> es2 = extract.allOperands(e2);
    assert es2 != null;
    final List<Expression> ret = an.empty.list(), different = an.empty.list();
    for (final Expression ¢ : es1) {
      assert ¢ != null;
      (isIn(¢, es2) ? ret : different).add(¢);
    }
    for (final Expression ¢ : es2) { // [a c]
      assert ¢ != null;
      if (!isIn(¢, ret))
        different.add(¢);
    }
    assert ret != null;
    if (!ret.isEmpty())
      different.removeAll(ret);
    assert the.firstOf(ret) != null;
    assert the.firstOf(different) != null;
    assert the.secondOf(different) != null;
    return subject.pair(the.firstOf(ret), //
        subject.pair(//
            the.firstOf(different), the.secondOf(different)//
        ).to(//
            op.PLUS2)//
    ).to(//
        TIMES//
    );
  }
  @SuppressWarnings("boxing") private ASTNode replacement(final List<Expression> xs) {
    if (xs.size() == 1)
      return az.infixExpression(the.firstOf(xs)).getOperator() != TIMES ? null : the.firstOf(xs);
    if (xs.size() == 2)
      return replacement(az.infixExpression(the.firstOf(xs)), az.infixExpression(the.secondOf(xs)));
    final List<Expression> ret = an.empty.list(), different = an.empty.list();
    List<Expression> temp = as.list(xs);
    for (final Integer i : range.from(0).to(xs.size())) {
      temp = the.tailOf(temp);
      for (final Expression op : extract.allOperands(az.infixExpression(xs.get(i)))) { // b
        for (final Expression ops : temp)
          if (isIn(op, extract.allOperands(az.infixExpression(ops))))
            addCommon(op, ret);
          else
            addDifferent(op, different);
        if (temp.size() == 1)
          extract.allOperands(az.infixExpression(the.firstOf(temp))).stream().filter(λ -> !isIn(λ, ret)).forEach(λ -> addDifferent(λ, different));
        lisp.removeFromList(different, ret);
      }
    }
    Expression addition = null;
    for (final Integer ¢ : range.from(0).to(different.size() - 1))
      addition = subject.pair(addition != null ? addition : different.get(¢), different.get(¢ + 1)).to(op.PLUS2);
    if (ret.isEmpty())
      return addition;
    if (ret.size() == 1)
      return subject.pair(the.firstOf(ret), addition).to(TIMES);
    if (ret.size() <= 1)
      return null;
    Expression multiplication = null;
    for (int ¢ = 0; ¢ < ret.size() - 1;) {
      ++¢;
      multiplication = (multiplication == null ? subject.pair(ret.get(¢), ret.get(¢ + 1)) : subject.pair(multiplication, different.get(¢ + 1))).to(TIMES);
    }
    return subject.pair(multiplication, addition).to(TIMES);
  }
}
