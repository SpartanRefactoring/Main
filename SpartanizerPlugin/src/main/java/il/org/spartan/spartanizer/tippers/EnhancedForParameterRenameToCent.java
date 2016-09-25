package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class EnhancedForParameterRenameToCent extends EagerWring<EnhancedForStatement> implements Kind.Centification {
  @Override public String description(final EnhancedForStatement ¢) {
    return "Rename '" + ¢.getParameter().getName() + "' to ¢ in enhanced for loop";
  }

  @Override public Tip suggest(final EnhancedForStatement s, final ExclusionManager m) {
    final SingleVariableDeclaration d = s.getParameter();
    final SimpleName n = d.getName();
    if (in(n.getIdentifier(), "$", "¢", "__", "_") || !isJohnDoe(d))
      return null;
    final Statement body = s.getBody();
    if (haz.variableDefinition(body) || Collect.usesOf(n).in(body).isEmpty())
      return null;
    final SimpleName ¢ = s.getAST().newSimpleName("¢");
    if (m != null)
      m.exclude(s);
    return new Tip(description(s), s, body) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, ¢, s, r, g);
      }
    };
  }
}
