package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import static il.org.spartan.refactoring.utils.extract.*;

/**
 * /** A {@link Wring} to convert <code>if (x) return b; else { }</code> into
 * <code>if (x) return b;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-01
 */
public final class IfDegenerateElse extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Simplify {
  @Override Statement replacement(final IfStatement s) {
    final IfStatement $ = duplicate(s);
    $.setElseStatement(null);
    return !Is.blockRequiredInReplacement(s, $) ? $ : Subject.statement($).toBlock();
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && then(s) != null && degenerateElse(s);
  }
  @Override String description(final IfStatement s) {
    return "Remove vacuous 'else' branch of if(" + s.getExpression() + ") ...";
  }
  static boolean degenerateElse(final IfStatement s) {
    return elze(s) != null && Is.vacuousElse(s);
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}