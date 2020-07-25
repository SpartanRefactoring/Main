package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue0968;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Test case is {@link Issue0968} Issue #968 convert {@code
 * int a = f(), b = g();
 * } to {@code
 * int a = f();
 * int b = g();
 * }
 * @author Tomer Dragucki
 * @since 19-12-2016 */
public class VariableDeclarationStatementSplit extends CarefulTipper<VariableDeclarationStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x257BFB652923B5C5L;

  @Override public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Split initialization statement";
  }
  @Override protected boolean prerequisite(final VariableDeclarationStatement ¢) {
    return fragments(¢).stream().filter(VariableDeclarationStatementSplit::isFragmentApplicable).count() >= 2;
  }
  @Override public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢), first = copy.of(¢);
    final VariableDeclarationFragment fs = getFirstAssignment($), ff = fragments(first).get(fragments($).indexOf(fs));
    fragments($).remove(fs);
    fragments(first).clear();
    fragments(first).add(ff);
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
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
