package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.range.*;

/** Apply the distributive rule to multiplication: {@code
* a*b + a*c
 * } to <code>
* <b>a * (b + c)</b>
 * </code> .
 * @author Matteo Orru'
 * @since 2016 */
public final class InfixMultiplicationDistributive extends ReplaceCurrentNode<InfixExpression>
    //
    implements TipperCategory.Arithmetic, TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -4040103682801205377L;

  private static boolean IsSimpleMultiplication(@NotNull final Expression $) {
    return !iz.simpleName($) && ((InfixExpression) $).getOperator() == TIMES;
  }

  @NotNull private static List<Expression> removeFirstElement(@NotNull final List<Expression> ¢) {
    @NotNull final List<Expression> $ = new ArrayList<>(¢);
    $.remove(first($));// remove first
    return $;
  }

  @Override public String description() {
    return "a*b + a*c => a * (b + c)";
  }

  @Override @NotNull public String description(final InfixExpression ¢) {
    return "Apply the distributive rule to " + ¢;
  }

  @Override public boolean prerequisite(@Nullable final InfixExpression $) {
    return $ != null && iz.infixPlus($) && IsSimpleMultiplication(left($)) && IsSimpleMultiplication(right($));
  }

  @Override public ASTNode replacement(@NotNull final InfixExpression ¢) {
    return ¢.getOperator() != PLUS ? null : replacement(extract.allOperands(¢));
  }

  private void addCommon(@NotNull final Expression op, @NotNull final Collection<Expression> common) {
    addNewInList(op, common);
  }

  private void addDifferent(@NotNull final Expression op, @NotNull final Collection<Expression> different) {
    addNewInList(op, different);
  }

  private void addNewInList(@NotNull final Expression item, @NotNull final Collection<Expression> xs) {
    if (!isIn(item, xs))
      xs.add(item);
  }

  @SuppressWarnings("static-method") private boolean isIn(@NotNull final Expression op, @NotNull final Collection<Expression> allOperands) {
    return allOperands.stream().anyMatch(λ -> wizard.same(op, λ));
  }

  @SuppressWarnings("static-method") private void removeElFromList(@NotNull final Iterable<Expression> items, @NotNull final List<Expression> from) {
    items.forEach(from::remove);
  }

  private ASTNode replacement(@NotNull final InfixExpression e1, @NotNull final InfixExpression e2) {
    assert e1 != null;
    assert e2 != null;
    @Nullable final List<Expression> es1 = extract.allOperands(e1);
    assert es1 != null;
    @Nullable final List<Expression> es2 = extract.allOperands(e2);
    assert es2 != null;
    @NotNull final List<Expression> $ = new ArrayList<>(), different = new ArrayList<>();
    for (@NotNull final Expression ¢ : es1) {
      assert ¢ != null;
      (isIn(¢, es2) ? $ : different).add(¢);
    }
    for (@NotNull final Expression ¢ : es2) { // [a c]
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

  @SuppressWarnings("boxing") private ASTNode replacement(@NotNull final List<Expression> xs) {
    if (xs.size() == 1)
      return az.infixExpression(first(xs)).getOperator() != TIMES ? null : first(xs);
    if (xs.size() == 2)
      return replacement(az.infixExpression(first(xs)), az.infixExpression(second(xs)));
    @NotNull final List<Expression> $ = new ArrayList<>(), different = new ArrayList<>();
    @NotNull List<Expression> temp = new ArrayList<>(xs);
    for (final Integer i : range.from(0).to(xs.size())) {
      temp = removeFirstElement(temp);
      for (@NotNull final Expression op : extract.allOperands(az.infixExpression(xs.get(i)))) { // b
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
    @Nullable Expression addition = null;
    for (final Integer ¢ : range.from(0).to(different.size() - 1))
      addition = subject.pair(addition != null ? addition : different.get(¢), different.get(¢ + 1)).to(PLUS2);
    if ($.isEmpty())
      return addition;
    if ($.size() == 1)
      return subject.pair(first($), addition).to(TIMES);
    if ($.size() <= 1)
      return null;
    @Nullable Expression multiplication = null;
    for (int ¢ = 0; ¢ < $.size() - 1;) {
      ++¢;
      multiplication = (multiplication == null ? subject.pair($.get(¢), $.get(¢ + 1)) : subject.pair(multiplication, different.get(¢ + 1))).to(TIMES);
    }
    return subject.pair(multiplication, addition).to(TIMES);
  }
}
