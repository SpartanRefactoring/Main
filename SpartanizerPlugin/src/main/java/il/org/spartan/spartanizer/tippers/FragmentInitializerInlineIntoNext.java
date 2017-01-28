package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** convert
 *
 * <pre>
 * a = 3;
 * whatever(a);
 * </pre>
 *
 * to
 *
 * <pre>
 * whatever(3);
 * </pre>
 *
 * @author Ori Marcovitch
 * @since 2016-11-27 */
public final class FragmentInitializerInlineIntoNext extends $FragementInitializerStatement implements TipperCategory.Inlining {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline assignment to " + name(¢) + " into next statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement next, final TextEditGroup g) {
    if (InliningUtilties.forbiddenOperationOnPrimitive(f, next))
      return null;
    final Statement parent = az.statement(f.getParent());
    if (parent == null || iz.forStatement(parent))
      return null;
    final SimpleName n1 = peelIdentifier(next, identifier(name(f)));
    if (n1 == null || anyFurtherUsage(parent, next, identifier(n1)) || leftSide(next, identifier(n1)) || preOrPostfix(n1))
      return null;
    Expression e = !iz.castExpression(initializer) ? initializer : subject.operand(initializer).parenthesis();
    final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
    if (pp != null)
      e = InliningUtilties.protect(e, pp);
    if (pp == null || fragments(pp).size() <= 1)
      $.remove(parent, g);
    else {
      if (type(pp).getNodeType() == ASTNode.ARRAY_TYPE)
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

  private static boolean preOrPostfix(final SimpleName id) {
    final ASTNode $ = parent(id);
    return iz.prefixExpression($) || iz.postfixExpression($);
  }

  private static boolean leftSide(final Statement next, final String id) {
    final Bool $ = new Bool();
    next.accept(new ASTVisitor() {
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
}
