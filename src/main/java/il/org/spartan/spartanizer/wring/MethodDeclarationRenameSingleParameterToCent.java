package il.org.spartan.spartanizer.wring;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** 
 * Convert <code>void f(int a){}</code> to <code>void f(int ¢){}</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class MethodDeclarationRenameSingleParameterToCent extends Wring<MethodDeclaration> implements Kind.Centification {
  @Override String description(final MethodDeclaration d) {
    return d.getName() + "";
  }

  @Override Rewrite make(final MethodDeclaration d, final ExclusionManager m) {
    assert d != null;
    if (d.isConstructor())
      return null;
    List<SingleVariableDeclaration> ps = parameters(d);
    if (ps.size() != 1)
      return null;
    final SimpleName n = first(ps).getName();
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢") || haz.variableDefinition(d.getBody()))
      return null;
    if (m != null)
      m.exclude(d);
    return new Rewrite("Rename paraemter " + n + " to ¢ ", d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Wrings.rename(n, cent(), d, r, g);
      }

      SimpleName cent() {
        return d.getAST().newSimpleName("¢");
      }
    };
  }
}
