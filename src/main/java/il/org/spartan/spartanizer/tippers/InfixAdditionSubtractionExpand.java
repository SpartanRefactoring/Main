package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** expand additive terms, e.g., convert {@code a-(b+c)} to {@code a-b-c}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM} */
public final class InfixAdditionSubtractionExpand extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Arithmetic {
  private static final long serialVersionUID = 8781877067464919917L;

  @Override public String description(final InfixExpression ¢) {
    return "Expand additive terms in " + ¢;
  }

  @Override public Expression replacement(final InfixExpression ¢) {
//    System.out.println("InfixExpression: " + ¢);
    if (TermsCollector.isLeafTerm(¢)){
//      System.out.println("LEAF!");
      return null;
    }
    final Expression $ = TermsExpander.simplify(¢);
//    System.out.println("$: " + $);
    Expression expression = !wizard.same2($, ¢) ? $ : null;
//    System.out.println("expression: " + expression);
    return expression;
  }
  
  @Override
  @SuppressWarnings("unused")
  protected boolean prerequisite(final InfixExpression __) {
    return true;
  }
}
