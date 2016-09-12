package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class EnhancedForRenameParameterToCent extends Wring<EnhancedForStatement> implements Kind.Centification {
  @Override String description(final EnhancedForStatement ¢) {
    return ¢.getParameter() + "";
  }

  @Override Rewrite make(final EnhancedForStatement s, final ExclusionManager m) {
    final Statement body = s.getBody();
    if (body == null)
      return null;
    final SingleVariableDeclaration d = s.getParameter();
    if (!isJohnDoe(d))
      return null;
    final SimpleName n = d.getName();
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_") || haz.variableDefinition(body) || Collect.usesOf(n).in(body).isEmpty())
      return null;
    //if (m != null)
      //m.exclude(body);
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Rewrite("Rename '" + n + "' to ¢ in enhanced for loop", d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, ¢, s, r, g);
      }
    };
  }
}
