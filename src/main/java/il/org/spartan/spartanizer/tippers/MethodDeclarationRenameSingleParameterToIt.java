package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code void f(int a){}} to {@code void f(int it){}} Test class is
 * Issue1162
 * @author Dor Ma'ayan
 * @since 2017-07-05 */
public final class MethodDeclarationRenameSingleParameterToIt extends EagerTipper<MethodDeclaration>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = 0x5583F2C8E00B4000L;

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override public Tip tip(final MethodDeclaration d, final ExclusionManager m) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d))
      return null;
    final SingleVariableDeclaration parameter = onlyOne(parameters(d));
    if (!JohnDoe.property(parameter))
      return null;
    final SimpleName $ = parameter.getName();
    assert $ != null;
    if (in($.getIdentifier(), namer.specials))
      return null;
    final Block b = body(d);
    if (b == null || haz.variableDefinition(b) || haz.it(b) || collect.usesOf($).in(b).isEmpty())
      return null;
    if (m != null)
      m.exclude(d);
    final SimpleName ¢ = namer.newIt(d);
    return new Tip("Rename paraemter " + $ + " to it ", getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        action.rename($, ¢, d, r, g);
        ParameterAbbreviate.fixJavadoc(d, $, ¢ + "", r, g);
      }
    };
  }
}
