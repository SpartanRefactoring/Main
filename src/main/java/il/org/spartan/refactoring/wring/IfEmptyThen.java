package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
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
public final class IfEmptyThen extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and remove vacuous 'then' branch";
  }
  @Override Statement replacement(final IfStatement s) {
    final IfStatement $ = Subject.pair(elze(s), null).toNot(s.getExpression());
    return !Is.blockRequiredInReplacement(s, $) ? $ : Subject.statement($).toBlock();
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && Is.vacuousThen(s) && !Is.vacuousElse(s);
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}