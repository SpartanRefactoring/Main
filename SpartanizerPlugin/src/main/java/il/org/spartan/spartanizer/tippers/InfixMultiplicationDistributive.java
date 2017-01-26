package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

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
 * @since 2016 */
public final class InfixMultiplicationDistributive extends ReplaceCurrentNode<InfixExpression>
    //
    implements TipperCategory.Arithmetic, TipperCategory.CommnonFactoring {
  private static boolean IsSimpleMultiplication(final Expression $) {
    return !iz.simpleName($) && ((InfixExpression) $).getOperator() == TIMES;
  }

  private static List<Expression> removeFirstElement(final List<Expression> ¢) {
    final List<Expression> $ = new ArrayList<>(¢);
    $.remove(first($));// remove first
    return $;
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
    return allOperands.stream().anyMatch(λ -> wizard.same(op, λ));
  }

  @SuppressWarnings("static-method") private void removeElFromList(final List<Expression> items, final List<Expression> from) {
    items.forEach(from::remove);
  }

  private ASTNode replacement(final InfixExpression e1, final InfixExpression e2) {
    assert e1 != null;
    assert e2 != null;
    final List<Expression> $ = new ArrayList<>(), different = new ArrayList<>(), es1 = extract.allOperands(e1);
    assert es1 != null;
    final List<Expression> es2 = extract.allOperands(e2);
    assert es2 != null;
    for (final Expression ¢ : es1) {
      assert ¢ != null;
      (isIn(¢, es2) ? $ : different).add(¢);
    }
    for (final Expression ¢ : es2) { // [a c]
      assert ¢ != null;
      if (!isIn(¢, $))
        different.add(¢);
    }
    assert $ != null;
    if (!$.isEmpty())
      different.remove($);
    assert first($) != null;
    assert first(different) != null;
    assert second(different) != null;
    return subject.pair(first($), //
        subject.pair(//
            first(different), second(different)//
        ).to(//
            PLUS2)//
    ).to(//
        TIMES//
    );
  }

  @SuppressWarnings("boxing") private ASTNode replacement(final List<Expression> xs) {
    if (xs.size() == 1)
      return az.infixExpression(first(xs)).getOperator() != TIMES ? null : first(xs);
    if (xs.size() == 2)
      return replacement(az.infixExpression(first(xs)), az.infixExpression(second(xs)));
    final List<Expression> $ = new ArrayList<>(), different = new ArrayList<>();
    List<Expression> temp = new ArrayList<>(xs);
    for (final Integer i : range.from(0).to(xs.size())) {
      temp = removeFirstElement(temp);
      for (final Expression op : extract.allOperands(az.infixExpression(xs.get(i)))) { // b
        for (final Expression ops : temp)
          if (isIn(op, extract.allOperands(az.infixExpression(ops))))
            addCommon(op, $);
          else
            addDifferent(op, different);
        if (temp.size() == 1)
          extract.allOperands(az.infixExpression(first(temp))).stream().filter(λ -> !isIn(λ, $)).forEach(λ -> addDifferent(λ, different));
        removeElFromList(different, $);
      }
    }
    Expression addition = null;
    for (final Integer ¢ : range.from(0).to(different.size() - 1))
      addition = subject.pair(addition != null ? addition : different.get(¢), different.get(¢ + 1)).to(PLUS2);
    Expression multiplication = null;
    if ($.isEmpty())
      return addition;
    if ($.size() == 1)
      return subject.pair(first($), addition).to(Operator.TIMES);
    if ($.size() <= 1)
      return null;
    for (int ¢ = 0; ¢ < $.size() - 1;) {
      ++¢;
      multiplication = (multiplication == null ? subject.pair($.get(¢), $.get(¢ + 1)) : subject.pair(multiplication, different.get(¢ + 1)))
          .to(Operator.TIMES);
    }
    return subject.pair(multiplication, addition).to(Operator.TIMES);
  }
}
