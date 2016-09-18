/** A visitor hack converting the type specific visit functions, into a single
 * call to {@link #go(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 * @author Yossi Gil
 * @year 2016
 * @see ExclusionManager */
package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Convert <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class ForRenameInitializerToCent extends Wring<VariableDeclarationExpression> implements Kind.Centification {
  @Override public String description(final VariableDeclarationExpression ¢) {
    return "Rename for iteration variable " + extract.onlyName(¢) + " to ¢";
  }

  @Override public Suggestion suggest(final VariableDeclarationExpression d, final ExclusionManager m) {
    final ForStatement forStatement = az.forStatement(parent(d));
    if (forStatement == null)
      return null;
    final SimpleName n = extract.onlyName(d);
    if (n == null)
      return null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_"))
      return null;
    if (!isJohnDoe(d.getType(), n))
      return null;
    final Statement body = forStatement.getBody();
    if (body == null)
      return null;
    if (haz.variableDefinition(body))
      return null;
    final List<SimpleName> uses = Collect.usesOf(n).in(body);
    if (uses.isEmpty())
      return null;
    if (m != null)
      m.exclude(d);
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Suggestion(description(d), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, ¢, forStatement, r, g);
      }
    };
  }
}
