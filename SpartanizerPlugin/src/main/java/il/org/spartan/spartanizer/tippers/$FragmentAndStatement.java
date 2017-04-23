package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public abstract class $FragmentAndStatement extends GoToNextStatement<VariableDeclarationFragment> {
  private static final long serialVersionUID = 0x1B70489B1D1340L;

  @Override public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && ¢ != null;
  }

  @Override public abstract String description(VariableDeclarationFragment f);

  static int eliminationSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = wizard.live(f, fragments(parent));
    final int $ = metrics.size(parent);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  public static Collection<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
    final Collection<VariableDeclarationFragment> $ = an.empty.list();
    boolean collecting = false;
    for (final VariableDeclarationFragment sibling : fragments((VariableDeclarationStatement) f.getParent())) {
      if (sibling == f) {
        collecting = true;
        continue;
      }
      if (collecting)
        $.add(sibling);
    }
    return $;
  }

  public static int removalSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final int $ = metrics.size(parent);
    if (parent.fragments().size() <= 1)
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    newParent.fragments().remove(parent.fragments().indexOf(f));
    return $ - metrics.size(newParent);
  }

  static boolean usedInSubsequentInitializers(final VariableDeclarationFragment f, final SimpleName n) {
    boolean searching = true;
    for (final VariableDeclarationFragment ff : fragments(az.variableDeclrationStatement(f.getParent())))
      if (searching)
        searching = ff != f;
      else if (!collect.usesOf(n).in(ff.getInitializer()).isEmpty())
        return true;
    return false;
  }

  protected abstract ASTRewrite go(ASTRewrite r, VariableDeclarationFragment f, SimpleName n, Expression initializer, Statement nextStatement,
      TextEditGroup g);

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement,
      final TextEditGroup g) {
    return !iz.variableDeclarationStatement(f.getParent()) ? null : go(r, f, f.getName(), f.getInitializer(), nextStatement, g);
  }

  @Override public final Tip tip(final VariableDeclarationFragment f) {
    return new Tip(description(f), myClass(), f) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        $FragmentAndStatement.this.go(r, f, extract.nextStatement(f.getParent()), g);
      }
    }.spanning(extract.nextStatement(f.getParent()));
  }

  public static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    return forbiddenSiblings(f).stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }
}
