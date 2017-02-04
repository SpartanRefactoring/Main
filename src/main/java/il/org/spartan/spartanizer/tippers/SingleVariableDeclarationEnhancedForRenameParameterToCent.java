package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Doron Meshulam: this is a redundant tipper, see #750 Convert
 *         {@code for(int i:as)sum+=i;} to {@code for(int ¢:as)sum+=¢;}
 * @author Yossi Gil
 * @since 2016-09 */
public final class SingleVariableDeclarationEnhancedForRenameParameterToCent extends EagerTipper<SingleVariableDeclaration>
    //
    implements TipperCategory.Centification {
  @Override @NotNull public String description(final SingleVariableDeclaration ¢) {
    return ¢ + "";
  }

  @Override public Tip tip(@NotNull final SingleVariableDeclaration d, @Nullable final ExclusionManager m) {
    final EnhancedForStatement $ = az.enhancedFor(parent(d));
    if ($ == null)
      return null;
    final ASTNode p1 = yieldAncestors.untilClass(MethodDeclaration.class).from($);
    if (p1 instanceof MethodDeclaration)
      for (final SingleVariableDeclaration x : parameters((MethodDeclaration) p1)) {
        final SimpleName sn = x.getName();
        assert sn != null;
        if (in(sn.getIdentifier(), namer.current))
          return null;
      }
    final Statement body = $.getBody();
    if (body == null || !JohnDoe.property(d))
      return null;
    final SimpleName n = d.getName();
    assert n != null;
    if (in(n.getIdentifier(), namer.specials) || haz.variableDefinition(body) || haz.cent(body))
      return null;
    final List<SimpleName> uses = collect.usesOf(n).in(body);
    assert uses != null;
    if (uses.isEmpty())
      return null;
    if (m != null)
      m.exclude(body);
    final SimpleName ¢ = namer.newCurrent(d);
    return isNameDefined($, ¢) ? null : new Tip("Rename '" + n + "' to ¢ in enhanced for loop", d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename(n, ¢, $, r, g);
      }
    };
  }

  private static boolean isNameDefined(@NotNull final Statement s, final SimpleName n) {
    final Statement $ = az.statement(s.getParent());
    return Environment
        .of($ == null ? s
            : iz.block($) ? lisp.last(step.statements(az.block($))) : iz.switchStatement($) ? lisp.last(step.statements(az.switchStatement($))) : s)
        .has(step.identifier(n));
  }
}
