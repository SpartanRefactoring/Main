package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert <code>catch(Exceprion e){}</code> to
 * <code>catch(Exceprion ¢){}</code>
 * @author Dor Ma'ayan
 * @since 22-11-2016 */
public final class CatchClauseRenameParameterToCent extends EagerTipper<CatchClause> implements TipperCategory.Centification {
  @Override public String description(@SuppressWarnings("unused") final CatchClause ¢) {
    return "Rename the parameter of the catch clause";
  }

  @Override public Tip tip(final CatchClause c, final ExclusionManager m) {
    assert c != null;
    final SingleVariableDeclaration parameter = c.getException();
    if (!JohnDoe.property(parameter))
      return null;
    final SimpleName $ = parameter.getName();
    assert $ != null;
    if (in($.getIdentifier(), "$", "¢", "__", "_"))
      return null;
    final Block b = c.getBody();
    if (b == null || haz.variableDefinition(b) || haz.cent(b) || Collect.usesOf($).in(b).isEmpty())
      return null;
    if (m != null)
      m.exclude(c);
    final SimpleName ¢ = c.getAST().newSimpleName("¢");
    return new Tip("Rename paraemter " + $ + " to ¢ ", c, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, c, r, g);
      }
    };
  }
}
