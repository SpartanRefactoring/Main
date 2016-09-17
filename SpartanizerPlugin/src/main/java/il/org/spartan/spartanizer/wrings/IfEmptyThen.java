package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

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
public final class IfEmptyThen extends ReplaceCurrentNode<IfStatement> implements Kind.Collapse {
  @Override public boolean demandsToSuggestButPerhapsCant(final IfStatement ¢) {
    return iz.vacuousThen(¢) && !iz.vacuousElse(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and remove vacuous 'then' branch";
  }

  @Override public Statement replacement(final IfStatement s) {
    final IfStatement $ = subject.pair(elze(s), null).toNot(s.getExpression());
    return !iz.blockRequiredInReplacement(s, $) ? $ : subject.statement($).toBlock();
  }
}
