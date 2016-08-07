package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>if (x) if (a) f();</code> into <code>if (x
 * && a) f();</code>
 *
 * @author Yossi Gil
 * @since 2015-09-01
 */
public final class IfThenIfThenNoElseNoElse extends Wring<IfStatement> implements Kind.ConsolidateStatements {
  @Override String description(final IfStatement s) {
    return "Merge conditionals of if(" + s.getExpression() + ") ... with its nested if";
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return make(s) != null;
  }
  @Override Rewrite make(final IfStatement s, final ExclusionManager exclude) {
    if (!Is.vacuousElse(s))
      return null;
    final IfStatement then = asIfStatement(extract.singleThen(s));
    if (then == null || !Is.vacuousElse(then))
      return null;
    if (exclude != null)
      exclude.exclude(then);
    return new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        collapse(Wrings.blockIfNeeded(s, r, g), r, g);
      }
    };
  }
  static void collapse(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    final IfStatement then = asIfStatement(extract.singleThen(s));
    final InfixExpression e = Subject.pair(s.getExpression(), then.getExpression()).to(CONDITIONAL_AND);
    r.replace(s.getExpression(), e, g);
    r.replace(then, duplicate(then(then)), g);
  }
  @Override Rewrite make(final IfStatement s) {
    return make(s, null);
  }
}