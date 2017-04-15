package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Use {@link #examples()} for documentation
 * @author Dor Ma'ayan
 * @since 22-11-2016 */
public final class CatchClauseRenameParameterToIt extends EagerTipper<CatchClause>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = -0x5C1F4985DCAC0D78L;

  @Override public String description(final CatchClause ¢) {
    return String.format("Rename caught %s (%s) to %s", //
        ¢.getException().getName(), //
        ¢.getException().getType(), //
        Namer.cent //
    );
  }

  @Override public Examples examples() {
    return //
    convert("try {f();} catch (Exception e) {e.printStackTrace();}") //
        .to("try {f();} catch (Exception ¢) {¢.printStackTrace();}") //
        .ignores("Exception ¢; try {f();} catch (Exception e) {e.printStackTrace();}") //
        .ignores("try {f();} catch (Exception e) {int ¢; e.printStackTrace();}") //
    ;
  }

  @Override public Tip tip(final CatchClause c) {
    final SingleVariableDeclaration d = c.getException();
    if (!JohnDoe.property(d))
      return null;
    final SimpleName $ = d.getName();
    if (Namer.isSpecial($))
      return null;
    final Block b = body(c);
    return b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty() ? null
        : new Tip(description(c), myClass(), c.getException().getName(), c) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            action.rename($, Namer.newCent($), c, r, g);
          }
        };
  }
}
