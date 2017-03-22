/* A visitor hack converting the type specific visit functions, into a single
 * call to {@link #main(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @see ExclusionManager */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code for(int i:as)sum+=i;} to {@code f(int ¢:as)sum+=¢;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-09 */
public final class ForRenameInitializerToCent extends EagerTipper<VariableDeclarationExpression>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -3634676628162924936L;

  @NotNull @Override public String description(final VariableDeclarationExpression ¢) {
    return "Rename iteration variable '" + extract.onlyName(¢) + "' of for loop to '¢'";
  }

  @Override public Fragment tip(@NotNull final VariableDeclarationExpression x, @Nullable final ExclusionManager m) {
    @Nullable final ForStatement $ = az.forStatement(parent(x));
    if ($ == null)
      return null;
    final SimpleName n = extract.onlyName(x);
    if (n == null || in(n.getIdentifier(), namer.specials) || !JohnDoe.property(x.getType(), n))
      return null;
    final Statement body = $.getBody();
    if (body == null || haz.variableDefinition(body) || haz.cent(body) || !Inliner.variableUsedInFor($, n))
      return null;
    if (m != null) {
      m.exclude(body);
      m.exclude($);
    }
    final SimpleName ¢ = namer.newCurrent(x);
    return new Fragment(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(n, ¢, $, r, g);
      }
    };
  }
}
