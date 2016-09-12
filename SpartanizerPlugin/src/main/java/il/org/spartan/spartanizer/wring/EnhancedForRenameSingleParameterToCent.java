package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert <code>void f(int a){}</code> to <code>void f(int ¢){}</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class EnhancedForRenameSingleParameterToCent extends Wring<EnhancedForStatement> implements Kind.Centification {
  @Override String description(final EnhancedForStatement ¢) {
    return ¢.getName() + "";
  }

  // TODO: Alex and Dan. Here you may want to test your environment on this one.
  @Override Rewrite make(final EnhancedForStatement s, final ExclusionManager m) {
    final Statement body = s.getBody();
    if (body == null)
      return null;
    SingleVariableDeclaration d = s.getParameter();
    if (!isJohnDoe(d))
      return null;
    final SimpleName n = d.getName();
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_") || haz.variableDefinition(body) || Collect.usesOf(n).in(body).isEmpty())
      return null;
    if (m != null)
      m.exclude(d);
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Rewrite("Rename paraemter " + n + " to ¢ ", d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, ¢, d, r, g);
      }
    };
  }
}
