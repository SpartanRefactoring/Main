package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.factory.atomic;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Use {@link #examples()} for documentation
 * @author Dor Ma'ayan
 * @since 22-11-2016 */
public final class CatchClauseRenameParameterToCent extends EagerTipper<CatchClause>//
    implements Nominal.Trivialization {
  private static final long serialVersionUID = 0x41D7C628EB0EF103L;

  @Override public String description(final CatchClause ¢) {
    return "Rename exception " + English.name(¢.getException()) + " caught in catch clause here to ¢";
  }
  @Override public Examples examples() {
    return //
    convert("try {f();} catch (Exception e) {e.f();}") //
        .to("try {f();} catch (Exception ¢) {¢.f();}") //
        .ignores("Exception ¢; try {f();} catch (Exception e) {e.f();}") //
        .ignores("try {f();} catch (Exception e) {int ¢; e.f();}")//
    ;
  }
  @Override public Tip tip(final CatchClause c) {
    final SingleVariableDeclaration parameter = c.getException();
    if (!JohnDoe.property(parameter))
      return null;
    final SimpleName $ = parameter.getName();
    if (notation.isSpecial($))
      return null;
    final Block b = body(c);
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || collect.usesOf($).in(b).isEmpty())
      return null;
    final SimpleName ¢ = atomic.newCent(c);
    return new Tip(description(c), getClass(), c.getException().getName()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, c, r, g);
      }
    }.spanning(c);
  }
}
