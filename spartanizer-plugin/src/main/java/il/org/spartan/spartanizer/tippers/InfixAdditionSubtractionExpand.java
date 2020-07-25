package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.java.TermsCollector;
import il.org.spartan.spartanizer.java.TermsExpander;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
    final Expression $ = TermsExpander.simplify(¢);
    return !wizard.eq2($, ¢) ? $ : null;
  }
  @Override @SuppressWarnings("unused") protected boolean prerequisite(final InfixExpression __) {
    return true;
  }
}
