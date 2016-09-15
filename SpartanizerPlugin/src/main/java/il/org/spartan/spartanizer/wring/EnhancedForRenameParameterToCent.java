package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Convert <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class EnhancedForRenameParameterToCent extends Wring<SingleVariableDeclaration> implements Kind.Centification {
  @Override public String description(final SingleVariableDeclaration ¢) {
    return ¢ + "";
  }

  @Override public Rewrite wring(final SingleVariableDeclaration d, final ExclusionManager m) {
    final ASTNode p = d.getParent();
    if (p == null || !(p instanceof EnhancedForStatement))
      return null;
    final EnhancedForStatement s = (EnhancedForStatement) p;
    final Statement body = s.getBody();
    if (body == null || !isJohnDoe(d))
      return null;
    final SimpleName n = d.getName();
    if (m != null)
      m.exclude(d);
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_") || haz.variableDefinition(body))
      return null;
    final List<SimpleName> uses = Collect.usesOf(n).in(body);
    assert uses != null;
    if (uses.isEmpty())
      return null;
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Rewrite("Rename '" + n + "' to ¢ in enhanced for loop", d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, ¢, s, r, g);
      }
    };
  }
}
