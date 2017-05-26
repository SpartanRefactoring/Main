package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code void f(int a){}} to {@code void f(int ¢){}}
 * @author Yossi Gil
 * @since 2016-09 */
public final class MethodDeclarationRenameSingleParameter extends EagerTipper<MethodDeclaration>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = 0x5583F2C8E00B4000L;

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration ¢) {
    return notation.cent + "";
  }
  @Override public Tip tip(final MethodDeclaration d) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d) || d.parameters().isEmpty())
      return null;
    final SingleVariableDeclaration parameter = the.onlyOneOf(parameters(d));
    if (parameter == null || !JohnDoe.property(parameter) && !"param".equals(parameter.getName() + "") && !"it".equals(parameter.getName() + "")
        && !"¢".equals(parameter.getName() + ""))
      return null;
    final SimpleName $ = parameter.getName();
    assert $ != null;
    if (notation.isSpecial($))
      return null;
    final Block b = body(d);
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty())
      return null;
    final SimpleName ¢ = make.newCent(d);
    return new Tip("Rename Single paraemter " + $ +" to the chosen prefrence", getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, d, r, g);
        ParameterAbbreviate.fixJavadoc(d, $, ¢ + "", r, g);
      }
    }.spanning(d);
  }
}
