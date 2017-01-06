/** A visitor hack converting the type specific visit functions, into a single
 * call to {@link #main(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 * @author Yossi Gil
 * @year 2016
 * @see ExclusionManager */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class ForRenameInitializerToCent extends EagerTipper<VariableDeclarationExpression> implements TipperCategory.Centification {
  @Override public String description(final VariableDeclarationExpression ¢) {
    return "Rename iteration variable '" + extract.onlyName(¢) + "' of for loop to '¢'";
  }

  @Override public Tip tip(final VariableDeclarationExpression x, final ExclusionManager m) {
    if (x == null)
      return null;
    final ForStatement $ = az.forStatement(parent(x));
    if ($ == null)
      return null;
    final SimpleName n = extract.onlyName(x);
    if (n == null || in(n.getIdentifier(), "$", "¢", "__", "_") || !JohnDoe.property(x.getType(), n))
      return null;
    final Statement body = $.getBody();
    if (body == null || haz.variableDefinition(body) || haz.cent(body) || !iz.variableUsedInFor($, n))
      return null;
    if (m != null) {
      m.exclude(body);
      m.exclude($);
    }
    final SimpleName ¢ = x.getAST().newSimpleName("¢");
    return new Tip(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(n, ¢, $, r, g);
      }
    };
  }
}
