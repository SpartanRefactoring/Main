package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0968} Issue #968 convert {@code
 * int a = f(), b = g();
 * } to {@code
 * int a = f();
 * int b = g();
 * }
 * @author Tomer Dragucki
 * @since 19-12-2016 */
public class VariableDeclarationStatementSplit extends CarefulTipper<VariableDeclarationStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 2701028813439219141L;

  @Override @NotNull public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Split initialization statement";
  }

  @Override protected boolean prerequisite(final VariableDeclarationStatement ¢) {
    return fragments(¢).stream().filter(VariableDeclarationStatementSplit::isFragmentApplicable).count() >= 2;
  }

  @Override @NotNull public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢), first = copy.of(¢);
    final VariableDeclarationFragment fs = getFirstAssignment($), ff = fragments(first).get(fragments($).indexOf(fs));
    fragments($).remove(fs);
    fragments(first).clear();
    fragments(first).add(ff);
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(parent(¢), Block.STATEMENTS_PROPERTY);
        l.insertAfter($, ¢, g);
        l.insertAfter(first, ¢, g);
        l.remove(¢, g);
      }
    };
  }

  private static VariableDeclarationFragment getFirstAssignment(final VariableDeclarationStatement ¢) {
    return step.fragments(¢).stream().filter(VariableDeclarationStatementSplit::isFragmentApplicable).findFirst().orElse(null);
  }

  private static boolean isFragmentApplicable(final VariableDeclarationFragment ¢) {
    return initializer(¢) != null;
  }
}
