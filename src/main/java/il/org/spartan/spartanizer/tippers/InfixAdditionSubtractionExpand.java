package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** expand additive terms, e.g., convert {@code a-(b+c)} to {@code a-b-c}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM} */
public final class InfixAdditionSubtractionExpand extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Arithmetic {
  private static final long serialVersionUID = 0x79DF7EAA1B654B6DL;

  @Override @NotNull public String description(final InfixExpression ¢) {
    return "Expand additive terms in " + ¢;
  }

  @Override public Expression replacement(@NotNull final InfixExpression ¢) {
    if (TermsCollector.isLeafTerm(¢))
      return null;
    @NotNull final Expression $ = TermsExpander.simplify(¢);
    return !wizard.same2($, ¢) ? $ : null;
  }

  @Override @SuppressWarnings("unused") protected boolean prerequisite(final InfixExpression __) {
    return true;
  }
}
