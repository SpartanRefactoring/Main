package il.org.spartan.spartanizer.tippers;

import static fluent.ly.the.last;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.parameters;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.factory.atomic;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.engine.nominal.JohnDoe;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.spartanizer.research.analyses.notation;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** TODO Doron Meshulam: this is a redundant tipper, see #750 Convert
 * {@code for(int i:as)sum+=i;} to {@code for(int ¢:as)sum+=¢;}
 * @author Yossi Gil
 * @since 2016-09 */
public final class ForParameterRenameToIt extends EagerTipper<SingleVariableDeclaration>
    //
    implements Category.Loops {
  private static final long serialVersionUID = 0x477D0D92045512DL;

  @Override public String description(final SingleVariableDeclaration ¢) {
    return ¢ + "";
  }
  @Override public Tip tip(final SingleVariableDeclaration d) {
    final EnhancedForStatement $ = az.enhancedFor(parent(d));
    if ($ == null)
      return null;
    final ASTNode p1 = yieldAncestors.untilClass(MethodDeclaration.class).from($);
    if (p1 instanceof MethodDeclaration)
      for (final SingleVariableDeclaration x : parameters((MethodDeclaration) p1)) {
        final SimpleName sn = x.getName();
        assert sn != null;
        if (is.in(sn.getIdentifier(), notation.cent))
          return null;
      }
    final Statement body = $.getBody();
    if (body == null || !JohnDoe.property(d))
      return null;
    final SimpleName n = d.getName();
    assert n != null;
    if (notation.isSpecial(n) || haz.variableDefinition(body) || haz.cent(body))
      return null;
    final List<SimpleName> uses = collect.usesOf(n).in(body);
    assert uses != null;
    if (uses.isEmpty())
      return null;
    final SimpleName ¢ = atomic.newCent(d);
    return isNameDefined($, ¢) ? null : new Tip("Rename iterator '" + n + "' to ¢ in enhanced for loop", getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename(n, ¢, $, r, g);
      }
    }.spanning(body);
  }
  private static boolean isNameDefined(final Statement s, final SimpleName n) {
    final Statement $ = az.statement(s.getParent());
    return Environment
        .of($ == null ? s : iz.block($) ? last(statements(az.block($))) : iz.switchStatement($) ? last(statements(az.switchStatement($))) : s)
        .has(identifier(n));
  }
}
