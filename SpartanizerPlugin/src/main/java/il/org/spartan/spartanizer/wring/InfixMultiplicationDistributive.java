package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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

  private static List<Expression> removeFirstEl(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>(xs);
    $.remove($.get(0));// remove first
    return $;
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

  @Override public boolean demandsToSuggestButPerhapsCant(final InfixExpression $) {
    return $ != null && iz.infixPlus($) && IsSimpleMultiplication(left($)) && IsSimpleMultiplication(right($)); // super.scopeIncludes($);
  }

  @Override public String description() {
    return "a*b + a*c => a * (b + c)";
  }

  @Override public String description(final InfixExpression ¢) {
    return "Apply the distributive rule to " + ¢;
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

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != PLUS ? null : replacement(extract.allOperands(¢));
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
    for (final Expression ¢ : es1) {
      assert ¢ != null;
      (isIn(¢, es2) ? common : different).add(¢);
    }
    for (final Expression ¢ : es2) { // [a c]
      assert ¢ != null;
      if (!isIn(¢, common))
        different.add(¢);
    }
    assert common != null;
    if (!common.isEmpty())
      different.remove(common);
    assert first(common) != null;
    assert first(different) != null;
    assert second(different) != null;
    return subject.pair(first(common), //
        subject.pair(//
            first(different), second(different)//
        ).to(//
            Operator.PLUS)//
    ).to(//
        Operator.TIMES//
    );
  }

  private ASTNode replacement(final List<Expression> xs) {
    if (xs.size() == 1)
      return az.infixExpression(first(xs)).getOperator() != TIMES ? null : first(xs);
    if (xs.size() == 2)
      return replacement(az.infixExpression(first(xs)), az.infixExpression(second(xs)));
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
