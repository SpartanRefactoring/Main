package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code for(int i:as)sum+=i;} to {@code f(int ¢:as)sum+=¢;}
 * @author Yossi Gil
 * @author Doron Meshulam
 * @since 2016-09 */
public final class EnhancedForParameterRenameToCent extends EagerTipper<EnhancedForStatement>//
    implements TipperCategory.Centification {
  @Override public String description(final EnhancedForStatement ¢) {
    return "Rename '" + ¢.getParameter().getName() + "' to ¢ in enhanced for loop";
  }

  @Override public Tip tip(final EnhancedForStatement s, final ExclusionManager m) {
    final MethodDeclaration p = yieldAncestors.untilClass(MethodDeclaration.class).from(s);
    if (p == null)
      return null;
    final SimpleName sn = name(onlyOne(parameters(p)));
    if (sn == null || in(sn.getIdentifier(), namer.current))
      return null;
    final SingleVariableDeclaration d = s.getParameter();
    final SimpleName $ = d.getName();
    if (namer.isSpecial($) || !JohnDoe.property(d))
      return null;
    final Statement body = body(s);
    if (haz.variableDefinition(body) || haz.cent(body) || collect.usesOf($).in(body).isEmpty())
      return null;
    final SimpleName ¢ = newCurrent(s);
    if (m != null)
      m.exclude(s);
    return new Tip(description(s), s, getClass(), body) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, s, r, g);
      }
    };
  }

  public SimpleName newCurrent(final EnhancedForStatement s) {
    return s.getAST().newSimpleName(namer.current);
  }
}
