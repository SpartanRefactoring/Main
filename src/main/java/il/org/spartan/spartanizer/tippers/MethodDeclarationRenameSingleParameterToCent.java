package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert <code>void f(int a){}</code> to <code>void f(int ¢){}</code>
 * @author Yossi Gil
 * @since 2016-09 */
public final class MethodDeclarationRenameSingleParameterToCent extends EagerTipper<MethodDeclaration>//
    implements TipperCategory.Centification {
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
    if (in($.getIdentifier(), "$", "¢", "__", "_"))
      return null;
    final Block b = body(d);
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty())
      return null;
    if (m != null)
      m.exclude(d);
    final SimpleName ¢ = d.getAST().newSimpleName("¢");
    return new Tip("Rename paraemter " + $ + " to ¢ ", d, getClass()) {
      @Override
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, d, r, g);
        SingleVariableDeclarationAbbreviation.fixJavadoc(d, $, ¢ + "", r, g);
      }
    };
  }
}
