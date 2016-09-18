package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Convert <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class EnhancedForRenameParameterToCent extends Wring<EnhancedForStatement> implements Kind.Centification {
  @Override public String description(final EnhancedForStatement ¢) {
    return "Rename '" + ¢.getParameter().getName() + "' to ¢ in enhanced for loop";
  }

  @Override public Suggestion suggest(final EnhancedForStatement s, final ExclusionManager m) {
    final SingleVariableDeclaration d = s.getParameter();
    final SimpleName n = d.getName();
    if (in(n.getIdentifier(), "$", "¢", "__", "_"))
      return null;
    if (!isJohnDoe(d))
      return null;
    final Statement body = s.getBody();
    if (haz.variableDefinition(body))
      return null;
    final SimpleName ¢ = s.getAST().newSimpleName("¢");
    if (!Collect.usesOf(¢).in(body).isEmpty())
      return null;
    if (m != null)
      m.exclude(s);
    return new Suggestion(description(s), s, body) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, ¢, s, r, g);
      }
    };
  }
}
