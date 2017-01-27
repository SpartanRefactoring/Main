/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
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
import il.org.spartan.spartanizer.utils.*;

public abstract class $FragementInitializerStatement extends $ReplaceToNextStatement<VariableDeclarationFragment> {
  @Override public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && ¢ != null;
  }

  @Override public abstract String description(VariableDeclarationFragment f);

  protected static boolean forbidden(final VariableDeclarationFragment f, final Expression initializer) {
    return initializer == null || haz.annotation(f);
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

  static boolean usedInSubsequentInitializers(final VariableDeclarationFragment f, final SimpleName n) {
    boolean found = false;
    for (final VariableDeclarationFragment ff : fragments(az.variableDeclrationStatement(f.getParent())))
      if (!found)
        found = ff == f;
      else if (!collect.usesOf(n).in(ff.getInitializer()).isEmpty())
        return true;
    return false;
  }

  static boolean containsLambda(final Statement next) {
    return !yieldDescendants.untilClass(LambdaExpression.class).from(next).isEmpty();
  }

  static boolean containsClassInstanceCreation(final Statement next) {
    return !yieldDescendants.untilClass(ClassInstanceCreation.class).from(next).isEmpty();
  }

  protected static boolean anyFurtherUsage(final Statement originalStatement, final Statement next, final String id) {
    final Bool $ = new Bool();
    final ASTNode parent = next.getParent();
    parent.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(next) && !¢.equals(originalStatement) && iz.statement(¢) && !occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  protected static List<SimpleName> occurencesOf(final ASTNode $, final String id) {
    return yieldDescendants.untilClass(SimpleName.class).suchThat(λ -> identifier(λ).equals(id)).from($);
  }

  protected abstract ASTRewrite go(ASTRewrite r, VariableDeclarationFragment f, SimpleName n, Expression initializer, Statement next,
      TextEditGroup g);

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement next, final TextEditGroup g) {
    if (forbiddenTarget(next))
      return null;
    final Expression x = getFrom(f);
    if (x == null || Inliner.forbiddenOperationOnPrimitive(f, next))
      return null;
    final SimpleName $ = f.getName();
    return $ == null ? null : go(r, f, $, x, next, g);
  }

  static Expression getFrom(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement s = az.variableDeclarationStatement(f.getParent());
    if (s == null || haz.annotation(s))
      return null;
    final Expression $ = f.getInitializer();
    return $ == null || haz.sideEffects($) ? null : null;
  }

  static boolean forbiddenTarget(final Statement target) {
    return target == null || containsClassInstanceCreation(target) || containsLambda(target) || false;
  }

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}
