package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * if (x) ; else {a;}
 * </pre>
 *
 * into
 *
 * <pre>
 * if (!x)
 * a;
 * </pre>
 *
 * .
 * @author Yossi Gil
 * @since 2015-08-26 */
public final class IfEmptyThen extends ReplaceCurrentNode<IfStatement> implements TipperCategory.Collapse {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and remove vacuous 'then' branch";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return iz.vacuousThen(¢) && !iz.vacuousElse(¢);
  }
  @Override public Statement replacement(final IfStatement ¢) {
    final IfStatement $ = subject.pair(elze(¢), null).toNot(¢.getExpression());
    return !iz.blockRequiredInReplacement(¢, $) ? $ : subject.statement($).toBlock();
  }
}
