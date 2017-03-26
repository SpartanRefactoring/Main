package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code for(int i:as)sum+=i;} to {@code f(int ¢:as)sum+=¢;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @author Doron Meshulam
 * @since 2016-09 */
public final class EnhancedForParameterRenameToIt extends EagerTipper<EnhancedForStatement>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -3945693304397811549L;

  @Override  public String description( final EnhancedForStatement ¢) {
    return "Rename '" + ¢.getParameter().getName() + "' to ¢ in enhanced for loop";
  }

  @Override public Tip tip( final EnhancedForStatement s, @Nullable final ExclusionManager m) {
    @Nullable final MethodDeclaration p = yieldAncestors.untilClass(MethodDeclaration.class).from(s);
    if (p == null)
      return null;
    final SimpleName sn = name(onlyOne(parameters(p)));
    if (sn == null || in(sn.getIdentifier(), namer.it))
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
        Tricks.rename($, ¢, s, r, g);
      }
    };
  }

  public static SimpleName newCurrent( final EnhancedForStatement ¢) {
    return ¢.getAST().newSimpleName(namer.it);
  }
}
