package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code for(int i:as)sum+=i;} to {@code f(int ¢:as)sum+=¢;}
 * @author Yossi Gil
 * @author Doron Meshulam
 * @since 2016-09 */
public final class EnhancedForParameterRenameToIt extends EagerTipper<EnhancedForStatement>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -0x36C1EB27B944CF5DL;

  @Override public String description(final EnhancedForStatement ¢) {
    return "Rename '" + ¢.getParameter().getName() + "' to ¢ in enhanced for loop";
  }

  @Override public Tip tip(final EnhancedForStatement s) {
    final MethodDeclaration p = yieldAncestors.untilClass(MethodDeclaration.class).from(s);
    if (p == null)
      return null;
    final SimpleName sn = name(onlyOne(parameters(p)));
    if (sn == null || in(sn.getIdentifier(), namer.cent))
      return null;
    final SingleVariableDeclaration d = s.getParameter();
    final SimpleName $ = d.getName();
    if (namer.isSpecial($) || !JohnDoe.property(d))
      return null;
    final Statement body = body(s);
    if (haz.variableDefinition(body) || haz.cent(body) || collect.usesOf($).in(body).isEmpty())
      return null;
    final SimpleName ¢ = newCurrent(s);
    return new Tip(description(s), myClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        action.rename($, ¢, s, r, g);
      }
    }.spanning(s);
  }

  public static SimpleName newCurrent(final EnhancedForStatement ¢) {
    return ¢.getAST().newSimpleName(namer.cent);
  }
}
