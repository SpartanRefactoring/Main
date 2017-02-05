package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;


/** expand additive terms, e.g., convert {@code a-(b+c)} to /**
 * code>a-b-c</code>
 * @author Yossi Gil
 * @since 2016 */
public final class InfixAdditionSubtractionExpand extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Arithmetic {
  @Override  public String description(final InfixExpression ¢) {
    return "Expand additive terms in " + ¢;
  }

  @Override public Expression replacement( final InfixExpression ¢) {
    if (TermsCollector.isLeafTerm(¢))
      return null;
    final Expression $ = TermsExpander.simplify(¢);
    return !wizard.same($, ¢) ? $ : null;
  }
}
