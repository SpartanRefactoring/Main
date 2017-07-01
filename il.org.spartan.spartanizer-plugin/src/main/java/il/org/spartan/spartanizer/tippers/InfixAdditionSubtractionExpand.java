package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** expand additive terms, e.g., convert {@code a-(b+c)} to {@code a-b-c}
 * @author Yossi Gil */
public final class InfixAdditionSubtractionExpand extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Theory.Arithmetics.Symbolic {
  private static final long serialVersionUID = 0x79DF7EAA1B654B6DL;

  @Override public String description(final InfixExpression ¢) {
    return "Expand additive terms in " + ¢;
  }
  @Override public Expression replacement(final InfixExpression ¢) {
    if (TermsCollector.isLeafTerm(¢))
      return null;
    final Expression ret = TermsExpander.simplify(¢);
    return !wizard.eq2(ret, ¢) ? ret : null;
  }
  @Override @SuppressWarnings("unused") protected boolean prerequisite(final InfixExpression __) {
    return true;
  }
}
