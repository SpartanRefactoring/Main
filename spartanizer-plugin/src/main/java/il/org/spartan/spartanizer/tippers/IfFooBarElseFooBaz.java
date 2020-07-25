package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.misc.insertBefore;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Converts {@code if (X) { foo(); bar(); } else { foo(); baz(); } } into
 * {@code
 * foo();
 * if (X)
 *   bar();
 * else
 *   baz();
 * }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfFooBarElseFooBaz extends EagerTipper<IfStatement>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x4A8DBCAB922657DBL;

  private static List<Statement> commonPrefix(final List<Statement> ss1, final List<Statement> ss2) {
    final List<Statement> $ = an.empty.list();
    for (; !ss1.isEmpty() && !ss2.isEmpty(); ss2.remove(0)) {
      final Statement s1 = the.firstOf(ss1);
      if (!wizard.eq(s1, the.firstOf(ss2)))
        break;
      $.add(s1);
      ss1.remove(0);
    }
    return $;
  }
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Extract commmon prefix of then and else branches to just before if statement";
  }
  @Override public Tip tip(final IfStatement s) {
    final List<Statement> $ = extract.statements(then(s));
    if ($.isEmpty())
      return null;
    final List<Statement> elze = extract.statements(elze(s));
    if (elze.isEmpty())
      return null;
    final int thenSize = $.size(), elzeSize = elze.size();
    final List<Statement> commonPrefix = commonPrefix($, elze);
    return commonPrefix.isEmpty() || commonPrefix.size() == thenSize && commonPrefix.size() == elzeSize && !sideEffects.free(s.getExpression()) ? null
        : new Tip(description(s), getClass(), s) {
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
            return replacement(s.getExpression(), subject.ss($).toOneStatementOrNull(), subject.ss(elze).toOneStatementOrNull());
          }
          IfStatement replacement(final Expression condition, final Statement trimmedThen, final Statement trimmedElse) {
            return trimmedThen == null && trimmedElse == null ? null
                : trimmedThen == null ? subject.pair(trimmedElse, null).toNot(condition) : subject.pair(trimmedThen, trimmedElse).toIf(condition);
          }
        };
  }
}
