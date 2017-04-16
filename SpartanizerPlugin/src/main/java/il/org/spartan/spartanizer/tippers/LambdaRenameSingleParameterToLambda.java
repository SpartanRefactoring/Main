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
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tested by {@link Issue1115}
 * @author Yossi Gil
 * @since 2016-09 */
public final class LambdaRenameSingleParameterToLambda extends EagerTipper<LambdaExpression>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -0x2CF705A7699A0E07L;

  @Override public String description(final LambdaExpression ¢) {
    return "Rename lambda parameter " + onlyOne(parameters(¢)) + " to " + Namer.lambda;
  }

  @Override public Tip tip(final LambdaExpression x) {
    final VariableDeclarationFragment f = az.variableDeclrationFragment(onlyOne(parameters(x)));
    if (f == null)
      return null;
    final SimpleName $ = f.getName();
    if (in($.getIdentifier(), Namer.lambda, Namer.anonymous, Namer.forbidden))
      return null;
    final Namespace n = Environment.of(x);
    if (n.has(Namer.lambda) || n.hasChildren())
      return null;
    final SimpleName ¢ = x.getAST().newSimpleName(Namer.lambda);
    return new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, x, r, g);
      }
    };
  }
}
