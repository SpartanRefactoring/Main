package il.org.spartan.bloater.bloaters;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0976} Issue #976 Convert: {@code if (a == b && c ==
 * d) { a = 5; } } to: {@code if (a == b) { if (c == d) { a = 5; } } }
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-09 */
public class LongIfBloater extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -1472927802038098123L;

  @Override @Nullable public ASTNode replacement(@NotNull final IfStatement ¢) {
    if (!shouldTip(¢))
      return null;
    @Nullable final InfixExpression $ = az.infixExpression(¢.getExpression());
    final IfStatement newThen = subject.pair(then(¢), null).toIf(!$.hasExtendedOperands() ? $.getRightOperand() : getReducedIEFromIEWithExtOp($));
    final Statement oldElse = ¢.getElseStatement();
    if (oldElse == null)
      return subject.pair(newThen, null).toIf($.getLeftOperand());
    newThen.setElseStatement(copy.of(oldElse));
    return subject.pair(newThen, copy.of(oldElse)).toIf($.getLeftOperand());
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace an if statement that contains && with two ifs";
  }

  private static Expression getReducedIEFromIEWithExtOp(@NotNull final InfixExpression ¢) {
    final InfixExpression $ = subject.pair(¢.getRightOperand(), first(extendedOperands(¢))).to(¢.getOperator());
    subject.append($, step.extendedOperands(¢));
    if (!$.extendedOperands().isEmpty())
      $.extendedOperands().remove(0);
    return $;
  }

  private static boolean shouldTip(@NotNull final IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd(az.infixExpression(¢.getExpression()));
  }
}
