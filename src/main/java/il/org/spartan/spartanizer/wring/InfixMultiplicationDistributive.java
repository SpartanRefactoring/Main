package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Apply the distributive rule to multiplication:
 *
 * <pre>
* <b>a*b + a*c</b>
 * </pre>
 *
 * to
 *
 * <pre>
* <b>a * (b + c)</b>
 * </pre>
 *
 * .
 * @author Matteo Orru'
 * @since 2015-07-17 */
public final class InfixMultiplicationDistributive extends ReplaceCurrentNode<InfixExpression> implements Kind.DistributiveRefactoring {
  private static boolean IsSimpleMultiplication(final Expression $) {
    return !iz.simpleName($) && ((InfixExpression) $).getOperator() == TIMES;
  }

  @Override public String description() {
    return "a*b + a*c => a * (b + c)";
  }

  @Override String description(final InfixExpression x) {
    return "Apply the distributive rule to " + x;
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != PLUS ? null : replacement(extract.allOperands(x));
  }

  @Override boolean scopeIncludes(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && IsSimpleMultiplication(step.left($)) && IsSimpleMultiplication(step.right($)); // super.scopeIncludes($);
  }

  private void addCommon(final Expression op, final List<Expression> common) {
    addNewInList(op, common);
  }

  private void addDifferent(final Expression op, final List<Expression> different) {
    addNewInList(op, different);
  }

  private void addNewInList(final Expression item, final List<Expression> xs) {
    if (!isIn(item, xs))
      xs.add(item);
  }

  @SuppressWarnings("static-method") private boolean isIn(final Expression op, final List<Expression> allOperands) {
    for (final Expression $ : allOperands)
      if (wizard.same(op, $))
        return true;
    return false;
  }

  @SuppressWarnings("static-method") private void removeElFromList(final List<Expression> items, final List<Expression> from) {
    for (final Expression item : items)
      from.remove(item);
  }

  @SuppressWarnings("static-method") private List<Expression> removeFirstEl(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>(xs);
    $.remove($.get(0));// remove first
    return $;
  }

  private ASTNode replacement(final InfixExpression e1, final InfixExpression e2) {
    assert e1 != null;
    assert e2 != null;
    final List<Expression> common = new ArrayList<>();
    final List<Expression> different = new ArrayList<>();
    final List<Expression> es1 = extract.allOperands(e1);
    assert es1 != null;
    final List<Expression> es2 = extract.allOperands(e2);
    assert es2 != null;
    for (final Expression e : es1) {
      assert e != null;
      (isIn(e, es2) ? common : different).add(e);
    }
    for (final Expression e : es2) { // [a c]
      assert e != null;
      if (!isIn(e, common))
        different.add(e);
    }
    assert common != null;
    if (!common.isEmpty())
      different.remove(common);
    assert lisp.first(common) != null;
    assert lisp.first(different) != null;
    assert lisp.second(different) != null;
    return subject.pair(lisp.first(common), //
        subject.pair(//
            lisp.first(different), lisp.second(different)//
        ).to(//
            Operator.PLUS)//
    ).to(//
        Operator.TIMES//
    );
  }

  private ASTNode replacement(final List<Expression> xs) {
    if (xs.size() == 1)
      return az.infixExpression(xs.get(0)).getOperator() != TIMES ? null : xs.get(0);
    if (xs.size() == 2)
      return replacement(az.infixExpression(xs.get(0)), az.infixExpression(xs.get(1)));
    final List<Expression> common = new ArrayList<>();
    final List<Expression> different = new ArrayList<>();
    List<Expression> temp = new ArrayList<>(xs);
    for (int i = 0; i < xs.size(); ++i) {
      System.out.println(" === " + xs.get(i));
      temp = removeFirstEl(temp);
      for (final Expression op : extract.allOperands(az.infixExpression(xs.get(i)))) { // b
        for (final Expression ops : temp)
          if (isIn(op, extract.allOperands(az.infixExpression(ops))))
            addCommon(op, common);
          else
            addDifferent(op, different);
        if (temp.size() == 1)
          for (final Expression $ : extract.allOperands(az.infixExpression(temp.get(0))))
            if (!isIn($, common))
              addDifferent($, different);
        removeElFromList(different, common);
      }
    }
    Expression addition = null;
    for (int i = 0; i < different.size() - 1; ++i)
      addition = subject.pair(addition != null ? addition : different.get(i), different.get(i + 1)).to(Operator.PLUS);
    Expression multiplication = null;
    if (common.isEmpty())
      return addition;
    if (common.size() == 1)
      return subject.pair(common.get(0), addition).to(Operator.TIMES);
    if (common.size() <= 1)
      return null;
    for (int i = 0; i < common.size() - 1; ++i)
      multiplication = (multiplication == null ? subject.pair(common.get(i), common.get(i + 1)) : subject.pair(multiplication, different.get(i + 1)))
          .to(Operator.TIMES);
    return subject.pair(multiplication, addition).to(Operator.TIMES);
  }
}
