package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.analyses.*;
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
        notation.cent //
    );
  }

  //TODO: Dor Maayan, fix this example test -rr
  @Override public Examples examples() {
    return null;
//    return //
//    convert("try {f();} catch (Exception e) {e.f();}") //
//        .to("try {f();} catch (Exception ¢) {¢.f();}") //
//        .ignores("Exception ¢; try {f();} catch (Exception e) {e.f();}") //
//        .ignores("try {f();} catch (Exception e) {int ¢; e.f();}") //
//    ;
  }

  @Override public Tip tip(final CatchClause c) {
    final SingleVariableDeclaration d = c.getException();
    if (!JohnDoe.property(d))
      return null;
    final SimpleName $ = d.getName();
    if (notation.isSpecial($))
      return null;
    final Block b = body(c);
    return b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty() ? null
        : new Tip(description(c), myClass(), c.getException().getName(), c) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            misc.rename($, make.newCent($), c, r, g);
          }
        };
  }
}
