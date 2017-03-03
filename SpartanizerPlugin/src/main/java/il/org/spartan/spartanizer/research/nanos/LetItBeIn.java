package il.org.spartan.spartanizer.research.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
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
    return letInliner.tip(¢) != null//
        && fragmentInliner.cantTip(¢);
  }

  static class LetInliner extends ReplaceToNextStatement<VariableDeclarationFragment> {
    private static final long serialVersionUID = 2390117956692878327L;

    @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
      if (!preDelegation(f, nextStatement))
        return null;
      final Statement parent = az.variableDeclarationStatement(parent(f));
      if (parent == null || anyFurtherUsage(parent(nextStatement), name(f)))
        return null;
      final Expression initializer = initializer(f);
      if (initializer == null)
        return null;
      final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
      Expression e = !iz.castExpression(initializer) ? initializer : subject.operand(initializer).parenthesis();
      if (pp != null)
        e = Inliner.protect(e, pp);
      if (pp == null || fragments(pp).size() <= 1)
        $.remove(parent, g);
      else {
        if (nodeType(type(pp)) == ASTNode.ARRAY_TYPE)
          return null;
        final VariableDeclarationStatement pn = copy.of(pp);
        final List<VariableDeclarationFragment> l = fragments(pp);
        for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
          if (l.get(¢).equals(f)) {
            fragments(pn).remove(¢);
            break;
          }
          if (iz.containsName(name(f), initializer(l.get(¢))))
            return null;
        }
        $.replace(parent, pn, g);
      }
      for (final SimpleName ¢ : peelIdentifiers(nextStatement, name(f)))
        $.replace(¢, e, g);
      return $;
    }

    private static List<SimpleName> peelIdentifiers(final Statement s, final SimpleName n) {
      return collect.usesOf(n).in(s);
    }

    private static boolean anyFurtherUsage(final ASTNode node, final SimpleName n) {
      return collect.forAllOccurencesExcludingDefinitions(n).in(node).isEmpty();
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
}
