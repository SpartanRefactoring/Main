package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** XXX: This is a bug of auto-laconize Converts
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
public final class IfThenFooBarElseFooBaz extends EagerTipper<IfStatement> implements TipperCategory.CommnoFactoring {
  private static List<Statement> commonPrefix(final List<Statement> ss1, final List<Statement> ss2) {
    final List<Statement> $ = new ArrayList<>();
    for (; !ss1.isEmpty() && !ss2.isEmpty(); ss2.remove(0)) {
      final Statement s1 = first(ss1);
      if (!wizard.same(s1, first(ss2)))
        break;
      $.add(s1);
      ss1.remove(0);
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Condolidate commmon prefix of then and else branches to just before if statement";
  }

  @Override public Tip tip(final IfStatement s) {
    final List<Statement> then = extract.statements(then(s));
    if (then.isEmpty())
      return null;
    final List<Statement> elze = extract.statements(elze(s));
    if (elze.isEmpty())
      return null;
    int thenSize = then.size();
    int elzeSize = elze.size();
    final List<Statement> commonPrefix = commonPrefix(then, elze);
    return commonPrefix.isEmpty() || (commonPrefix.size() == thenSize && commonPrefix.size() == elzeSize && !sideEffects.free(s.getExpression()))
        ? null : new Tip(description(s), s, this.getClass()) {
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

  @Override public Tip tip(final IfStatement s, final ExclusionManager exclude) {
    return super.tip(s, exclude);
  }
}
