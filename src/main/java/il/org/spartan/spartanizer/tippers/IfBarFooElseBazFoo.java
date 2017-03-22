package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (X) { bar(); foo(); } else { baz(); foo(); } } into {@code
 * if (X)
 *   bar();
 * else
 *   baz();
 * foo();
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-09-05 */
public final class IfBarFooElseBazFoo extends EagerTipper<IfStatement>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -3692738201124876878L;

  @NotNull private static List<Statement> commmonSuffix(@NotNull final List<Statement> ss1, @NotNull final List<Statement> ss2) {
    @NotNull final List<Statement> $ = new ArrayList<>();
    for (; !ss1.isEmpty() && !ss2.isEmpty(); ss2.remove(ss2.size() - 1)) {
      final Statement s1 = last(ss1);
      if (!wizard.same(s1, last(ss2)))
        break;
      $.add(s1);
      ss1.remove(ss1.size() - 1);
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate commmon suffix of then and else branches to just after if statement";
  }

  @Override public Fragment tip(@NotNull final IfStatement s) {
    @NotNull final List<Statement> $ = extract.statements(then(s));
    if ($.isEmpty())
      return null;
    @NotNull final List<Statement> elze = extract.statements(elze(s));
    if (elze.isEmpty())
      return null;
    @NotNull final List<Statement> commmonSuffix = commmonSuffix($, elze);
    for (@NotNull final Statement st : commmonSuffix) {
      @NotNull final DefinitionsCollector c = new DefinitionsCollector($);
      st.accept(c);
      if (c.notAllDefined())
        return null;
    }
    return $.isEmpty() && elze.isEmpty() || commmonSuffix.isEmpty() ? null : new Fragment(description(s), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        @Nullable final IfStatement newIf = replacement();
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

      @Nullable IfStatement replacement() {
        return replacement(s.getExpression(), subject.ss($).toOneStatementOrNull(), subject.ss(elze).toOneStatementOrNull());
      }

      @Nullable IfStatement replacement(final Expression condition, @Nullable final Statement trimmedThen, @Nullable final Statement trimmedElse) {
        return trimmedThen == null && trimmedElse == null ? null
            : trimmedThen == null ? subject.pair(trimmedElse, null).toNot(condition) : subject.pair(trimmedThen, trimmedElse).toIf(condition);
      }
    };
  }

  @Override public Fragment tip(final IfStatement s, final ExclusionManager exclude) {
    return super.tip(s, exclude);
  }

  private static class DefinitionsCollector extends ASTVisitor {
    private boolean allDefined = true;
    @NotNull private final Statement[] statements;

    DefinitionsCollector(@NotNull final List<Statement> statements) {
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
