package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tippers.LocalInitializedInlineIntoNext;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.Tip;

/** Assignment then returnStatement or ExpressionsStatement using it, where the
 * assignment could not be inlined
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-01 */
public final class LetInNext extends NanoPatternTipper<VariableDeclarationFragment> {
  private static final long serialVersionUID = -0x6932FA8215A65359L;
  private static final LetInliner letInliner = new LetInliner();
  private static final LocalInitializedInlineIntoNext fragmentInliner = new LocalInitializedInlineIntoNext();

  @Override public boolean canTip(final VariableDeclarationFragment ¢) {
    return letInliner.canTip(¢)//
        && fragmentInliner.cantTip(¢);
  }

  static class LetInliner extends GoToNextStatement<VariableDeclarationFragment> {
    private static final long serialVersionUID = 0x212B679E43F623F7L;

    @Override public boolean prerequisite(final VariableDeclarationFragment f) {
      final Statement nextStatement = extract.nextStatement(f);
      final VariableDeclarationStatement $ = az.variableDeclarationStatement(parent(f));
      return preDelegation(f, nextStatement) && $ != null && fragments($).size() == 1 && noFurtherUsage(name(f), nextStatement)
          && initializer(f) != null;
    }
    @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
      final VariableDeclarationStatement parent = az.variableDeclarationStatement(parent(f));
      final Expression initializer = initializer(f);
      final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
      Expression e = !iz.castExpression(initializer) ? initializer : subject.operand(initializer).parenthesis();
      if (pp != null)
        e = Inliner.protect(e);
      $.remove(parent, g);
      $.replace(nextStatement, make.ast((!iz.returnStatement(nextStatement) ? "" : "return ") + "let(()->" + initializer + ").in(" + name(f) + "->"
          + expression(nextStatement) + ");"), g);
      return $;
    }
    private static boolean noFurtherUsage(final SimpleName n, final Statement nextStatement) {
      final List<SimpleName> $ = collect.forAllOccurencesExcludingDefinitions(n).in(parent(nextStatement));
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
    @Override public String description(@SuppressWarnings("unused") final VariableDeclarationFragment __) {
      return "inline me!";
    }
  }

  @Override protected Tip pattern(final VariableDeclarationFragment ¢) {
    return letInliner.tip(¢);
  }
  @Override public String tipperName() {
    return "LetInNext";
  }
}
