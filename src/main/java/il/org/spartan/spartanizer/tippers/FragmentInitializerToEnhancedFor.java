package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** convert <code>int a=E;for(e: C(a)) S;</code> to 
 * <code>for(e: C(E)) S;</code> to <code>for(int a=3;p;) {++i;}</code>
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-27 */
public final class FragmentInitializerToEnhancedFor extends $ReplaceToNextStatement<VariableDeclarationFragment> //
implements TipperCategory.Inlining {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline assignment to " + name(¢) + " into next statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    final Expression initializer = f.getInitializer();
    if (initializer == null)
      return null;
    final EnhancedForStatement s = az.enhancedFor(nextStatement);
    if (s == null)
      return null;
    final Statement body = s.getBody();
    if (containsClassInstanceCreation(nextStatement) || containsLambda(nextStatement))
      return null;
    final SingleVariableDeclaration z = s.getParameter();
    final Expression zz = s.getExpression();
    final Statement parent = az.statement(f.getParent());
    if (parent == null || iz.forStatement(parent))
      return null;
    final SimpleName n = peelIdentifier(nextStatement, identifier(name(f)));
    if (n == null || anyFurtherUsage(parent, nextStatement, identifier(n)) || leftSide(nextStatement, identifier(n)) || preOrPostfix(n))
      return null;
    Expression e = !iz.castExpression(initializer) ? initializer : subject.operand(initializer).parenthesis();
    final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
    if (pp != null)
      e = InliningUtilties.protect(e, pp);
    if (pp == null || fragments(pp).size() <= 1)
      $.remove(parent, g);
    else {
      if (step.type(pp).getNodeType() == ASTNode.ARRAY_TYPE)
        return null;
      final VariableDeclarationStatement pn = copy.of(pp);
      final List<VariableDeclarationFragment> l = fragments(pp);
      for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
        if (l.get(¢).equals(f)) {
          fragments(pn).remove(¢);
          break;
        }
        if (iz.containsName(f.getName(), l.get(¢).getInitializer()))
          return null;
      }
      $.replace(parent, pn, g);
    }
    $.replace(n, e, g);
    return $;
  }

  private static boolean containsLambda(final Statement nextStatement) {
    return !yieldDescendants.untilClass(LambdaExpression.class).from(nextStatement).isEmpty();
  }

  private static boolean preOrPostfix(final SimpleName id) {
    final ASTNode $ = parent(id);
    return iz.prefixExpression($) || iz.postfixExpression($);
  }

  private static boolean containsClassInstanceCreation(final Statement nextStatement) {
    return !yieldDescendants.untilClass(ClassInstanceCreation.class).from(nextStatement).isEmpty();
  }

  private static boolean anyFurtherUsage(final Statement originalStatement, final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    final ASTNode parent = nextStatement.getParent();
    parent.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(nextStatement) && !¢.equals(originalStatement) && iz.statement(¢) && !occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  private static boolean leftSide(final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    nextStatement.accept(new ASTVisitor() {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢)) && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }

  private static SimpleName peelIdentifier(final Statement s, final String id) {
    final List<SimpleName> $ = occurencesOf(s, id);
    return $.size() != 1 ? null : first($);
  }

  static List<SimpleName> occurencesOf(final ASTNode $, final String id) {
    return yieldDescendants.untilClass(SimpleName.class).suchThat(λ -> identifier(λ).equals(id)).from($);
  }
}