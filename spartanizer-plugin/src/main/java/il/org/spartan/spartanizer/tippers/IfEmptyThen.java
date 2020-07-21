package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code if (x) ; else {a;} } into {@code
 * if (!x)
 * a;
 * } .
 * @author Yossi Gil
 * @since 2015-08-26 */
public final class IfEmptyThen extends ReplaceCurrentNode<IfStatement>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x1D4E8820DEF9BF53L;

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
