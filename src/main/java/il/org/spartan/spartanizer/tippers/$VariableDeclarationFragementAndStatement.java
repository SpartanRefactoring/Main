package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
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

abstract class $VariableDeclarationFragementAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {
  static Expression assignmentAsExpression(final Assignment ¢) {
    final Operator $ = ¢.getOperator();
    return $ == ASSIGN ? copy.of(from(¢)) : subject.pair(to(¢), from(¢)).to(wizard.assign2infix($));
  }

  static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    for (final VariableDeclarationFragment ¢ : forbiddenSiblings(f)) // NANO?
      if (Collect.BOTH_SEMANTIC.of(¢).existIn(ns))
        return true;
    return false;
  }

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @param f
   * @param r
   * @param g */
  static void eliminate(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = live(f, fragments(parent));
    if (live.isEmpty()) {
      r.remove(parent, g);
      return;
    }
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent, newParent, g);
  }

  static int eliminationSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = live(f, fragments(parent));
    final int $ = metrics.size(parent);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  static int removalSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final int $ = metrics.size(parent);
    if (parent.fragments().size() <= 1)
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    newParent.fragments().remove(parent.fragments().indexOf(f));
    return $ - metrics.size(newParent);
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param f
   * @param r
   * @param g */
  static void remove(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    r.remove(parent.fragments().size() > 1 ? f : parent, g);
  }

  private static List<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment brother : fragments((VariableDeclarationStatement) f.getParent())) {
      if (brother == f) {
        collecting = true;
        continue;
      }
      if (collecting)
        $.add(brother);
    }
    return $;
  }

  private static List<VariableDeclarationFragment> live(final VariableDeclarationFragment f, final List<VariableDeclarationFragment> fs) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    fs.stream().filter(brother -> brother != null && brother != f && brother.getInitializer() != null).forEach(brother -> $.add(copy.of(brother)));
    return $;
  }

  @Override public Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }

  protected abstract ASTRewrite go(ASTRewrite r, VariableDeclarationFragment f, SimpleName n, Expression initializer, Statement nextStatement,
      TextEditGroup g);

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement,
      final TextEditGroup g) {
    if (!iz.variableDeclarationStatement(f.getParent()))
      return null;
    final SimpleName $ = f.getName();
    return $ == null ? null : go(r, f, $, f.getInitializer(), nextStatement, g);
  }

  static boolean usedInSubsequentInitializers(final VariableDeclarationFragment f, final SimpleName n) {
    boolean found = false;
    for (final VariableDeclarationFragment ff : fragments(az.variableDeclrationStatement(f.getParent())))
      if (!found)
        found = ff == f;
      else if (!Collect.usesOf(n).in(ff.getInitializer()).isEmpty())
        return true;
    return false;
  }

  protected boolean forbidden(final VariableDeclarationFragment f, final Expression initializer) {
    return initializer == null || haz.annotation(f);
  }
}