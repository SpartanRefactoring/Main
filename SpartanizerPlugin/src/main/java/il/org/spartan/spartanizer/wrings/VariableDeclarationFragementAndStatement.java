package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

public abstract class VariableDeclarationFragementAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {
  protected static Expression assignmentAsExpression(final Assignment a) {
    final Operator o = a.getOperator();
    return o == ASSIGN ? duplicate.of(from(a)) : subject.pair(to(a), from(a)).to(wizard.assign2infix(o));
  }

  protected static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    for (final VariableDeclarationFragment ¢ : forbiddenSiblings(f))
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
  protected static void eliminate(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = live(f, fragments(parent));
    if (live.isEmpty()) {
      r.remove(parent, g);
      return;
    }
    final VariableDeclarationStatement newParent = duplicate.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent, newParent, g);
  }

  protected static int eliminationSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = live(f, fragments(parent));
    final int $ = metrics.size(parent);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = duplicate.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  protected static int removalSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final int $ = metrics.size(parent);
    if (parent.fragments().size() <= 1)
      return $;
    final VariableDeclarationStatement newParent = duplicate.of(parent);
    newParent.fragments().remove(parent.fragments().indexOf(f));
    return $ - metrics.size(newParent);
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node left empty, it is removed as well.
   * @param f
   * @param r
   * @param g */
  protected static void remove(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    r.remove(parent.fragments().size() > 1 ? f : parent, g);
  }

  static List<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    for (final VariableDeclarationFragment brother : fragments(parent)) {
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
    for (final VariableDeclarationFragment brother : fs)
      if (brother != null && brother != f && brother.getInitializer() != null)
        $.add(duplicate.of(brother));
    return $;
  }

  @Override public Suggestion suggest(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Suggestion $ = super.suggest(f, exclude);
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
    final SimpleName n = f.getName();
    return n == null ? null : go(r, f, n, f.getInitializer(), nextStatement, g);
  }
}