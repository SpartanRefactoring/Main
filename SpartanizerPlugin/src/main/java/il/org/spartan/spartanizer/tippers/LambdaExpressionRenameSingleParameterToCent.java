package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tested by {@link Issue1115}
 * @author Yossi Gil
 * @since 2016-09 */
public final class LambdaExpressionRenameSingleParameterToCent extends EagerTipper<LambdaExpression>//
    implements TipperCategory.Centification {
  @Override public String description(final LambdaExpression ¢) {
    return "Rename parameter " + onlyOne(parameters(¢)) + " to ¢ ";
  }

  @Override public Tip tip(final LambdaExpression x, final ExclusionManager m) {
    final VariableDeclaration vd = onlyOne(parameters(x));
    if (vd == null)
      return null;
    final SimpleName $ = vd.getName();
    if (in($.getIdentifier(), namer.standardNames))
      return null;
    final Namespace n = Environment.of(x);
    if (n.has(namer.current) || !n.allowsCurrent())
      return null;
    if (m != null)
      m.exclude(x);
    final SimpleName ¢ = x.getAST().newSimpleName("¢");
    return new Tip(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, x, r, g);
      }
    };
  }
}
