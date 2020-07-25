package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.spartanizer.ast.navigate.step.extendedOperands;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import fluent.ly.forget;
import il.org.spartan.Wrapper;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;

/** Relic;
 * @author Artium Nihamkin
 * @since 2013/01/01 */
public final class Wizard extends RefactoringWizard {
  /** @param refactoring the refactoring to be used with this wizard */
  public Wizard(final Refactoring refactoring) {
    super(refactoring, PREVIEW_EXPAND_FIRST_NODE);
  }
  @Override protected void addUserInputPages() {
    forget.em(new Object[] { this });
  }
  public static InfixExpression append(final InfixExpression base, final Iterable<Expression> adds) {
    final Wrapper<InfixExpression> $ = new Wrapper<>(base);
    adds.forEach(λ -> $.set(append($.get(), λ)));
    return $.get();
  }
  public static InfixExpression append(final InfixExpression base, final Expression add) {
    final InfixExpression $ = copy.of(base);
    extendedOperands($).add(make.plant(copy.of(add)).into($));
    return $;
  }
}
