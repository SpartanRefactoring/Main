package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert <code>void f(int a){}</code> to <code>void f(int ¢){}</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class MethodDeclarationRenameSingleParameterToCent extends EagerTipper<MethodDeclaration> implements TipperCategory.Centification {
  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override public Tip tip(final MethodDeclaration d, final ExclusionManager m) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d))
      return null;
    final SingleVariableDeclaration parameter = onlyOne(parameters(d));
    if (!isJohnDoe(parameter))
      return null;
    final SimpleName n = parameter.getName();
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_"))
      return null;
    final Block b = d.getBody();
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || Collect.usesOf(n).in(b).isEmpty())
      return null;
    if (m != null)
      m.exclude(d);
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Tip("Rename paraemter " + n + " to ¢ ", d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(n, ¢, d, r, g);
        SingleVariableDeclarationAbbreviation.fixJavadoc(d, n, ¢ + "", r, g);
      }
    };
  }
}
