package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

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

/** convert {@code int a = 3; while(Panic) { ++OS.is.in.danger; } } to
 * {@code for(int a = 3; Panic;) { ++OS.is.in.danger; } }
 * @author Alex Kopzon
 * @since 2016 */
public final class FragmentInitializerWhile extends ReplaceToNextStatementExclude<VariableDeclarationFragment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 0x7B0F45FEAE8DEEA5L;

  private static VariableDeclarationStatement fragmentParent(final VariableDeclarationFragment ¢) {
    return copy.of(az.variableDeclrationStatement(parent(¢)));
  }

  // TODO: now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final WhileStatement s) {
    return fragments(vds).stream()
        .allMatch(λ -> collect.variableUsedInWhile(s, name(λ)) && Inliner.variableNotUsedAfterStatement(az.statement(s), λ.getName()));
  }

  @Nullable public static Expression Initializers(final VariableDeclarationFragment ¢) {
    return make.variableDeclarationExpression(fragmentParent(¢));
  }

  @Nullable public static VariableDeclarationStatement parent(final VariableDeclarationFragment ¢) {
    return az.variableDeclrationStatement(step.parent(¢));
  }

  @NotNull public static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? wizard.goInfix(copy.of(az.infixExpression(from)), s)
        : iz.assignment(from) ? FragmentInitializerToForInitializers.handleAssignmentCondition(az.assignment(from), s)
            : iz.parenthesizedExpression(from)
                ? FragmentInitializerToForInitializers.handleParenthesizedCondition(az.parenthesizedExpression(from), s) : from;
  }

  @Override @NotNull public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subsequent 'while', making a 'for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + ";)' loop";
  }

  @Override @Nullable protected ASTRewrite go(@Nullable final ASTRewrite $, @Nullable final VariableDeclarationFragment f,
      @Nullable final Statement nextStatement, final TextEditGroup g, @Nullable final ExclusionManager exclude) {
    if (f == null || $ == null || nextStatement == null || exclude == null)
      return null;
    @Nullable final VariableDeclarationStatement vds = parent(f);
    if (vds == null)
      return null;
    @Nullable final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null || !fragmentsUseFitting(vds, s))
      return null;
    exclude.excludeAll(fragments(vds));
    $.remove(vds, g);
    $.replace(s, wizard.buildForStatement(f, s), g);
    return $;
  }
}
