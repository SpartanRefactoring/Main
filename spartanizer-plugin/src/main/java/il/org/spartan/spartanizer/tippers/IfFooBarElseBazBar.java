package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.misc.insertAfter;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code if (X) { bar(); foo(); } else { baz(); foo(); } } into {@code
 * if (X)
 *   bar();
 * else
 *   baz();
 * foo();
 * }
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class IfFooBarElseBazBar extends EagerTipper<IfStatement>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x333F3DD43690324EL;

  private static List<Statement> commmonSuffix(final List<Statement> ss1, final List<Statement> ss2) {
    final List<Statement> $ = an.empty.list();
    for (; !ss1.isEmpty() && !ss2.isEmpty(); ss2.remove(ss2.size() - 1)) {
      final Statement s1 = the.lastOf(ss1);
      if (!wizard.eq(s1, the.lastOf(ss2)))
        break;
      $.add(s1);
      ss1.remove(ss1.size() - 1);
    }
    return $;
  }
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Extract commmon suffix of then and else branches to just after if statement";
  }
  @Override public Tip tip(final IfStatement s) {
    final List<Statement> $ = extract.statements(then(s));
    if ($.isEmpty())
      return null;
    final List<Statement> elze = extract.statements(elze(s));
    if (elze.isEmpty())
      return null;
    final List<Statement> commmonSuffix = commmonSuffix($, elze);
    for (final Statement st : commmonSuffix) {
      final DefinitionsCollector c = new DefinitionsCollector($);
      st.accept(c);
      if (c.notAllDefined())
        return null;
    }
    return $.isEmpty() && elze.isEmpty() || commmonSuffix.isEmpty() ? null : new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement newIf = replacement();
        if (iz.block(s.getParent())) {
          final ListRewrite lr = insertAfter(s, commmonSuffix, r, g);
          lr.insertAfter(newIf, s, g);
          lr.remove(s, g);
        } else {
          if (newIf != null)
            commmonSuffix.add(0, newIf);
          r.replace(s, subject.ss(commmonSuffix).toBlock(), g);
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

  private static class DefinitionsCollector extends ASTVisitor {
    private boolean allDefined = true;
    private final Statement[] statements;

    DefinitionsCollector(final List<Statement> statements) {
      allDefined = true;
      this.statements = statements.toArray(new Statement[statements.size()]);
    }
    public boolean notAllDefined() {
      return !allDefined;
    }
    @Override public boolean visit(final SimpleName ¢) {
      if (!collect.declarationsOf(¢).in(statements).isEmpty())
        allDefined = false;
      return false;
    }
  }
}
