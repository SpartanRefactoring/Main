package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.tipping.*;
import nano.ly.*;

/** Tested by {@link Issue1115}
 * @author Yossi Gil
 * @since 2016-09 */
public final class LambdaRenameSingleParameterToLambda extends EagerTipper<LambdaExpression>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -0x2CF705A7699A0E07L;

  @Override public String description(final LambdaExpression ¢) {
    return "Rename lambda parameter " + the.onlyOneOf(parameters(¢)) + " to " + notation.lambda;
  }

  @Override public Tip tip(final LambdaExpression x) {
    final VariableDeclarationFragment f = az.variableDeclrationFragment(the.onlyOneOf(parameters(x)));
    if (f == null)
      return null;
    final SimpleName $ = f.getName();
    if (in($.getIdentifier(), notation.lambda, notation.anonymous, notation.forbidden))
      return null;
    final Namespace n = Environment.of(x);
    if (n.has(notation.lambda) || n.hasChildren())
      return null;
    final SimpleName ¢ = x.getAST().newSimpleName(notation.lambda);
    return new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, x, r, g);
      }
    };
  }
}
