package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** @mdoron this is a redundant tipper, see #750 Convert
 *         <code>for(int i:as)sum+=i;</code> to <code>f(int ¢:as)sum+=¢;</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class SingleVariableDeclarationEnhancedForRenameParameterToCent extends EagerTipper<SingleVariableDeclaration>
    implements TipperCategory.Centification {
  @Override public String description(final SingleVariableDeclaration ¢) {
    return ¢ + "";
  }

  @Override public Tip tip(final SingleVariableDeclaration d, final ExclusionManager m) {
    final ASTNode p = d.getParent();
    if (p == null || !(p instanceof EnhancedForStatement))
      return null;
    final EnhancedForStatement $ = (EnhancedForStatement) p;
    final ASTNode p1 = searchAncestors.forClass(MethodDeclaration.class).from($);
    if (p1 instanceof MethodDeclaration) {
      final List<SingleVariableDeclaration> l = parameters((MethodDeclaration) p1);
      if (l.size() == 1) {
        final SimpleName sn = onlyOne(l).getName();
        assert sn != null;
        if (in(sn.getIdentifier(), "¢"))
          return null;
      }
    }
    final Statement body = $.getBody();
    if (body == null || !JohnDoe.property(d))
      return null;
    final SimpleName n = d.getName();
    assert n != null;
    if (in(n.getIdentifier(), "$", "¢", "__", "_") || haz.variableDefinition(body) || haz.cent(body))
      return null;
    final List<SimpleName> uses = Collect.usesOf(n).in(body);
    assert uses != null;
    if (uses.isEmpty())
      return null;
    if (m != null)
      m.exclude(d);
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Tip("Rename '" + n + "' to ¢ in enhanced for loop", d, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(n, ¢, $, r, g);
      }
    };
  }
}
