package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

/**
 * A {@link Wring} to simplify a conditional expression containing a null this
 * comes in two varieties: <code>unless ? null : v</code> or <code>when ?
 * expression : null</code>. <p> Which are converted to
 * <code>eval(expression).unless(unless)</code> or
 * <code>eval(expression).when(when)</code>
 *
 * @author Yossi Gil
 * @since 2016
 */
public class TernaryOfNull extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarize {
  @Override Expression replacement(final ConditionalExpression e) {
    final Expression elze = elze(e);
    final boolean whenForm = Is._null(elze);
    final Expression condition = extract.condition(e);
    return replacement(!whenForm ? elze : then(e), whenForm ? condition : logicalNot(condition));
  }
  private static Expression replacement(final Expression eval, final Expression when) {
    return replacement(Subject.operand(eval).toMethodInvocation("eval"), when, logicalNot(when));
  }
  private static Expression replacement(final MethodInvocation $, final Expression when, final Expression unless) {
    return extract($, Subject.operand(when).toMethodInvocation("when"), Subject.operand(unless).toMethodInvocation("unless"));
  }
  private static Expression extract(final MethodInvocation $, final MethodInvocation when, final MethodInvocation unless) {
    return Subject.operand($).toChainInvocation(Wrings.length(when) <= Wrings.length(unless) ? when : unless);
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return Is._null(elze(e)) ^ Is._null(extract.then(e));
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate nested conditional expression";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}