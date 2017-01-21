package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0968} Issue #968 convert <code>
 * int a = f(), b = g();
 * </code> to <code>
 * int a = f();
 * int b = g();
 * </code>
 * @author Tomer Dragucki
 * @since 19-12-2016 */
public class VariableDeclarationStatementSplit extends CarefulTipper<VariableDeclarationStatement>//
    implements TipperCategory.Bloater {
  @Override public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Split initialization statement";
  }

  @Override protected boolean prerequisite(final VariableDeclarationStatement s) {
    int $ = 0;
    for (final VariableDeclarationFragment ¢ : step.fragments(s)) // NANO?
      if (isFragmentApplicable(¢))
        ++$;
    return $ >= 2;
  }

  @Override public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢), first = copy.of(¢);
    final VariableDeclarationFragment fs = getFirstAssignment($), ff = (VariableDeclarationFragment) first.fragments().get($.fragments().indexOf(fs));
    $.fragments().remove(fs);
    first.fragments().clear();
    step.fragments(first).add(ff);
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter($, ¢, g);
        l.insertAfter(first, ¢, g);
        l.remove(¢, g);
      }
    };
  }

  private static VariableDeclarationFragment getFirstAssignment(final VariableDeclarationStatement ¢) {
    for (final VariableDeclarationFragment $ : step.fragments(¢)) // NANO?
      if (isFragmentApplicable($))
        return $;
    return null;
  }

  private static boolean isFragmentApplicable(final VariableDeclarationFragment ¢) {
    return ¢.getInitializer() != null;
  }
}
