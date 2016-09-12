package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert <code>void f(int a){}</code> to <code>void f(int ¢){}</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class MethodDeclarationRenameSingleParameterToCent extends Wring<MethodDeclaration> implements Kind.Centification {
  @Override String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  // TODO: Alex and Dan. Here you may want to test your environment on this one.
  @Override Rewrite make(final MethodDeclaration d, final ExclusionManager m) {
    assert d != null;
    if (d.isConstructor() || iz.__abstract(d))
      return null;
    final Block b = d.getBody();
    if (b == null)
      return null;
    final List<SingleVariableDeclaration> ps = parameters(d);
    if (ps.size() != 1)
      return null;
    final SimpleName n = first(ps).getName();
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_") || haz.variableDefinition(d.getBody()) || Collect.usesOf(n).in(b).isEmpty())
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
