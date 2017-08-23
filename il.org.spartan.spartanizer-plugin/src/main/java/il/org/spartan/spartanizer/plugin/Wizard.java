package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.ui.refactoring.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;

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
