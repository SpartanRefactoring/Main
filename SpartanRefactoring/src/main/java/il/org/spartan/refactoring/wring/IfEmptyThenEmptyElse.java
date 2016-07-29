package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} to eliminate degenerate if statements such as
 * <code>if (x) ; else ;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-26
 */
public final class IfEmptyThenEmptyElse extends Wring<IfStatement> {
  @Override final Rewrite make(final IfStatement s) {
    return new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        s.setElseStatement(null);
        r.remove(s, g);
      }
    };
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && Is.vacuousThen(s) && Is.vacuousElse(s);
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove 'if' statement with vacous 'then' and 'else' parts";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.REFACTOR_INEFFECTIVE;
  }
}