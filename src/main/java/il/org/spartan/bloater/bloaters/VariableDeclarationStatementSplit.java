package il.org.spartan.bloater.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
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
public class VariableDeclarationStatementSplit extends CarefulTipper<VariableDeclarationStatement> implements TipperCategory.Expander {
  @Override public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Split initialization statement";
  }
  // TODO: Tomer Dragucki use class step if necessary and remove
  // @SuppressWarnings("unchecked") --yg

  @Override @SuppressWarnings("unchecked") protected boolean prerequisite(final VariableDeclarationStatement s) {
    int $ = 0;
    for (final VariableDeclarationFragment ¢ : (List<VariableDeclarationFragment>) s.fragments())
      if (isFragmentApplicable(¢))
        ++$;
    return $ >= 2;
  }
  // TODO: Tomer Dragucki use class step if necessary and remove
  // @SuppressWarnings("unchecked") --yg

  @Override @SuppressWarnings("unchecked") public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢), first = copy.of(¢);
    final VariableDeclarationFragment fs = getFirstAssignment($);
    final VariableDeclarationFragment ff = (VariableDeclarationFragment) first.fragments().get($.fragments().indexOf(fs));
    $.fragments().remove(fs);
    first.fragments().clear();
    first.fragments().add(ff);
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter($, ¢, g);
        l.insertAfter(first, ¢, g);
        l.remove(¢, g);
      }
    };
  }
  // TODO: Tomer Dragucki use class step if necessary and remove
  // @SuppressWarnings("unchecked") --yg

  @SuppressWarnings("unchecked") private static VariableDeclarationFragment getFirstAssignment(final VariableDeclarationStatement ¢) {
    for (final VariableDeclarationFragment $ : (List<VariableDeclarationFragment>) ¢.fragments())
      if (isFragmentApplicable($))
        return $;
    return null;
  }

  private static boolean isFragmentApplicable(final VariableDeclarationFragment ¢) {
    return ¢.getInitializer() != null;
  }
}
