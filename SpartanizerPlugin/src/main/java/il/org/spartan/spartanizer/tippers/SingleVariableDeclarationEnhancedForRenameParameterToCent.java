package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;


import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.namespace.*;
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
    if (p1 instanceof MethodDeclaration)
      for (final SingleVariableDeclaration x : parameters((MethodDeclaration) p1)) {
        final SimpleName sn = x.getName();
        assert sn != null;
        if (in(sn.getIdentifier(), "¢"))
          return null;
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
    return isNameDefined($, ¢) ? null : new Tip("Rename '" + n + "' to ¢ in enhanced for loop", d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(n, ¢, $, r, g);
      }
    };
  }
  
  private static boolean isNameDefined(Statement s, SimpleName n) {
    return Environment.of(s).has(step.identifier(n));
  }
}
