package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tested by {@link Issue1115}
 * @author Yossi Gil
 * @since 2016-09 */
public final class LambdaRenameSingleParameterToLambda extends EagerTipper<LambdaExpression>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -3240064673505742343L;

  @Override public String description(final LambdaExpression ¢) {
    return "Rename parameter " + onlyOne(parameters(¢)) + " to " + namer.lambda;
  }

  @Override public Tip tip(final LambdaExpression x, final ExclusionManager m) {
    final VariableDeclarationFragment f = az.variableDeclrationFragment(onlyOne(parameters(x)));
    if (f == null)
      return null;
    final SimpleName $ = f.getName();
    if (in($.getIdentifier(), namer.lambda, namer.anonymous, namer.forbidden))
      return null;
    final Namespace n = Environment.of(x);
    if (n.has(namer.lambda) || n.hasChildren())
      return null;
    if (m != null)
      m.exclude(x);
    final SimpleName ¢ = x.getAST().newSimpleName(namer.lambda);
    return new Tip(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, x, r, g);
      }
    };
  }
}
