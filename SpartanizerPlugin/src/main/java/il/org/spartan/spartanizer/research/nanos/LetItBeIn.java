package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Assignment then returnStatement or ExpressionsStatement using it, where the
 * assignment could not be inlined
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-01 */
public final class LetItBeIn extends NanoPatternTipper<VariableDeclarationFragment> {
  private static final long serialVersionUID = -7580396559433880409L;
  private static final LetInliner letInliner = new LetInliner();
  private static final FragmentInitializerInlineIntoNext fragmentInliner = new FragmentInitializerInlineIntoNext();

  @Override public boolean canTip(final VariableDeclarationFragment ¢) {
    return letInliner.canTip(¢)//
        && fragmentInliner.cantTip(¢);
  }

  static class LetInliner extends ReplaceToNextStatement<VariableDeclarationFragment> {
    private static final long serialVersionUID = 2390117956692878327L;

    @Override public boolean prerequisite(final VariableDeclarationFragment f) {
      @Nullable final Statement nextStatement = extract.nextStatement(f);
      @Nullable final VariableDeclarationStatement $ = az.variableDeclarationStatement(parent(f));
      return preDelegation(f, nextStatement) && $ != null && fragments($).size() == 1 && noFurtherUsage(name(f), nextStatement)
          && initializer(f) != null;
    }

    @Override @NotNull protected ASTRewrite go(@NotNull final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement,
        final TextEditGroup g) {
      @Nullable final VariableDeclarationStatement parent = az.variableDeclarationStatement(parent(f));
      @NotNull final Expression initializer = initializer(f);
      @Nullable final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
      Expression e = !iz.castExpression(initializer) ? initializer : subject.operand(initializer).parenthesis();
      if (pp != null)
        e = Inliner.protect(e, pp);
      $.remove(parent, g);
      $.replace(nextStatement, wizard.ast((!iz.returnStatement(nextStatement) ? "" : "return ") + "let(()->" + initializer + ").in(" + name(f) + "->"
          + expression(nextStatement) + ");"), g);
      return $;
    }

    private static boolean noFurtherUsage(final SimpleName n, final Statement nextStatement) {
      @Nullable final List<SimpleName> $ = collect.forAllOccurencesExcludingDefinitions(n).in(parent(nextStatement));
      $.remove(n);
      $.removeAll(collect.forAllOccurencesExcludingDefinitions(n).in(nextStatement));
      return $.isEmpty();
    }

    private static boolean preDelegation(final VariableDeclarationFragment f, final Statement nextStatement) {
      return (iz.expressionStatement(nextStatement)//
          || iz.returnStatement(nextStatement)) && usesAssignment(f, nextStatement);
    }

    private static boolean usesAssignment(final VariableDeclarationFragment f, final Statement nextStatement) {
      return !collect.usesOf(name(f)).in(nextStatement).isEmpty();
    }

    @Override @NotNull public String description(@SuppressWarnings("unused") final VariableDeclarationFragment __) {
      return "inline me!";
    }
  }

  @Override @Nullable protected Tip pattern(final VariableDeclarationFragment ¢) {
    return letInliner.tip(¢);
  }

  @Override public String nanoName() {
    return "LetInNext";
  }
}
