package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Use {@link #examples()} for documentation
 * @author Dor Ma'ayan
 * @since 22-11-2016 */
public final class CatchClauseRenameParameterToCent extends EagerTipper<CatchClause>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -6638105215049141624L;

  @Override @NotNull public String description(@NotNull final CatchClause ¢) {
    return  String.format("Rename caught %s (%s) to %s",
        ¢.getException().getName(), //
        ¢.getException().getType(), // 
       namer.lambda); 
  }

  @Override @NotNull public Example[] examples() {
    return new Example[] { //
        convert("try {f();} catch (Exception e) {e.printStackTrace();}") //
            .to("try {f();} catch (Exception ¢) {¢.printStackTrace();}"), //
        ignores("Exception ¢; try {f();} catch (Exception e) {e.printStackTrace();}"), //
        ignores("try {f();} catch (Exception e) {int ¢; e.printStackTrace();}") };
  }

  @Override public Tip tip(@NotNull final CatchClause c, @Nullable final ExclusionManager m) {
    final SingleVariableDeclaration parameter = c.getException();
    if (!JohnDoe.property(parameter))
      return null;
    final SimpleName $ = parameter.getName();
    if (namer.isSpecial($))
      return null;
    @NotNull final Block b = body(c);
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty())
      return null;
    if (m != null)
      m.exclude(c);
    final SimpleName ¢ = namer.newCurrent(c);
    return new Tip(description(c), c.getException().getName(), getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tricks.rename($, ¢, c, r, g);
      }
    };
  }
}
