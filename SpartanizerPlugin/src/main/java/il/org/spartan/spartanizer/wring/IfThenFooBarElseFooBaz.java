package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.wring.dispatch.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert
 *
 * <pre>
 * if (X) {
 *   foo();
 *   bar();
 * } else {
 *   foo();
 *   baz();
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * foo();
 * if (X)
 *   bar();
 * else
 *   baz();
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThenFooBarElseFooBaz extends Wring<IfStatement> implements Kind.DistributiveRefactoring {
  private static List<Statement> commonPrefix(final List<Statement> ss1, final List<Statement> ss2) {
    final List<Statement> $ = new ArrayList<>();
    while (!ss1.isEmpty() && !ss2.isEmpty()) {
      final Statement s1 = first(ss1);
      final Statement s2 = first(ss2);
      if (!wizard.same(s1, s2))
        break;
      $.add(s1);
      ss1.remove(0);
      ss2.remove(0);
    }
    return $;
  }

  @Override public boolean demandsToSuggestButPerhapsCant(final IfStatement ¢) {
    return suggest(¢) != null;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Condolidate commmon prefix of then and else branches to just before if statement";
  }

  @Override public Suggestion suggest(final IfStatement s) {
    final List<Statement> then = extract.statements(then(s));
    if (then.isEmpty())
      return null;
    final List<Statement> elze = extract.statements(elze(s));
    if (elze.isEmpty())
      return null;
    final List<Statement> commonPrefix = commonPrefix(then, elze);
    return commonPrefix.isEmpty() ? null : new Suggestion(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement newIf = replacement();
        if (!iz.block(s.getParent())) {
          if (newIf != null)
            commonPrefix.add(newIf);
          r.replace(s, subject.ss(commonPrefix).toBlock(), g);
        } else {
          final ListRewrite lr = insertBefore(s, commonPrefix, r, g);
          if (newIf != null)
            lr.insertBefore(newIf, s, g);
          lr.remove(s, g);
        }
      }

      IfStatement replacement() {
        return replacement(s.getExpression(), subject.ss(then).toOneStatementOrNull(), subject.ss(elze).toOneStatementOrNull());
      }

      IfStatement replacement(final Expression condition, final Statement trimmedThen, final Statement trimmedElse) {
        return trimmedThen == null && trimmedElse == null ? null
            : trimmedThen == null ? subject.pair(trimmedElse, null).toNot(condition) : subject.pair(trimmedThen, trimmedElse).toIf(condition);
      }
    };
  }

  @Override public Suggestion suggest(final IfStatement s, final ExclusionManager exclude) {
    return super.suggest(s, exclude);
  }
}
