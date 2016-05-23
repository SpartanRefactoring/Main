package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.elze;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.refactoring.utils.Funcs.then;
import static il.org.spartan.refactoring.wring.Wrings.insertBefore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (X) {foo(); bar();} else {foo();
 * baz();}</code> into <code>foo(); if (X) bar(); else baz();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfThenFooBarElseFooBaz extends Wring<IfStatement> {
  @Override String description(final IfStatement s) {
    return "Condolidate commmon prefix of then and else branches to just before if(" + s.getExpression() + ") ...";
  }
  @Override Rewrite make(final IfStatement s) {
    final List<Statement> then = Extract.statements(then(s));
    if (then.isEmpty())
      return null;
    final List<Statement> elze = Extract.statements(elze(s));
    if (elze.isEmpty())
      return null;
    final List<Statement> commonPrefix = commonPrefix(then, elze);
    return commonPrefix.isEmpty() ? null : new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement newIf = replacement();
        if (!Is.block(s.getParent())) {
          if (newIf != null)
            commonPrefix.add(newIf);
          scalpel.operate(s).replaceWith(Subject.ss(commonPrefix).toBlock());
        } else {
          insertBefore(s, commonPrefix, r, g);
          if (newIf == null)
            scalpel.operate(s).remove();
          else
            scalpel.operate(s).replaceWith(newIf);
        }
      }
      private IfStatement replacement() {
        return replacement(scalpel.duplicate(s.getExpression()), Subject.ss(scalpel.duplicate(then)).toOneStatementOrNull(),
            Subject.ss(scalpel.duplicate(elze)).toOneStatementOrNull());
      }
      private IfStatement replacement(final Expression condition, final Statement trimmedThen, final Statement trimmedElse) {
        return trimmedThen == null && trimmedElse == null ? null //
            : trimmedThen == null ? Subject.pair(trimmedElse, null).toNot(condition)//
                : Subject.pair(trimmedThen, trimmedElse).toIf(condition);
      }
    };
  }
  private List<Statement> commonPrefix(final List<Statement> ss1, final List<Statement> ss2) {
    final List<Statement> $ = new ArrayList<>();
    while (!ss1.isEmpty() && !ss2.isEmpty()) {
      final Statement s1 = ss1.get(0);
      final Statement s2 = ss2.get(0);
      if (!same(s1, s2))
        break;
      $.add(scalpel.duplicateWith(s1, s2));
      ss1.remove(0);
      ss2.remove(0);
    }
    return $;
  }
  @Override Rewrite make(final IfStatement s, final ExclusionManager exclude) {
    return super.make(s, exclude);
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return make(s) != null;
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}