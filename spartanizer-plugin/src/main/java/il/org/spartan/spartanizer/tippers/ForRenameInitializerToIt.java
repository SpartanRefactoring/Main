/* A visitor hack converting the __ specific visit functions, into a single call
 * to {@link #main(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 *
 * @author Yossi Gil
 *
 * @see ExclusionManager */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.atomic;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.nominal.JohnDoe;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.research.analyses.notation;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Nominal;

/** Convert {@code for(int i:as)sum+=i;} to {@code f(int ¢:as)sum+=¢;}
 * @author Yossi Gil
 * @since 2016-09 */
public final class ForRenameInitializerToIt extends EagerTipper<VariableDeclarationExpression>//
    implements Nominal.Trivialization {
  private static final long serialVersionUID = -0x3270F722CF10D188L;

  @Override public String description(final VariableDeclarationExpression ¢) {
    return "Rename iteration variable '" + extract.onlyName(¢) + "' to '¢'";
  }
  @Override public Tip tip(final VariableDeclarationExpression x) {
    final ForStatement $ = az.forStatement(parent(x));
    if ($ == null)
      return null;
    final SimpleName n = extract.onlyName(x);
    if (n == null || notation.isSpecial(n) || !JohnDoe.property(x.getType(), n))
      return null;
    final Statement body = $.getBody();
    if (body == null || haz.variableDefinition(body) || haz.cent(body) || !Inliner.variableUsedInFor($, n))
      return null;
    final SimpleName ¢ = atomic.newCent(x);
    return new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename(n, ¢, $, r, g);
      }
    }.spanning($);
  }
}
