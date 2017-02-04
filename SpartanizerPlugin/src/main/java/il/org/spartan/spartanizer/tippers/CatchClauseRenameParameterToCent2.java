package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Convert {@code catch(Exceprion e){}} to {@code catch(Exceprion ¢){}}
 * @author Dor Ma'ayan
 * @since 22-11-2016 */
public final class CatchClauseRenameParameterToCent2 extends EagerTipper<CatchClause>//
    implements TipperCategory.Centification {
  @Override @NotNull public String description(@NotNull final CatchClause ¢) {
    return "Rename exception " + ¢.getException().getNodeType() + " caught in catch clause here to ¢";
  }

  @Override public Tip tip(@NotNull final CatchClause c, @Nullable final ExclusionManager m) {
    assert c != null;
    final SingleVariableDeclaration parameter = c.getException();
    if (!JohnDoe.property(parameter))
      return null;
    final SimpleName $ = parameter.getName();
    assert $ != null;
    if (namer.isSpecial($))
      return null;
    final Block b = body(c);
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty())
      return null;
    if (m != null)
      m.exclude(c);
    final SimpleName ¢ = namer.newCurrent(c);
    return new Tip(description(c), c.getException().getName(), getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, c, r, g);
      }
    };
  }
}
