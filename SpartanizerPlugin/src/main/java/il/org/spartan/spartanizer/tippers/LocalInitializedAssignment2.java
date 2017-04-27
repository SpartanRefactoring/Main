package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedAssignment2 extends LocalInitializedStatement {
  private static final long serialVersionUID = 0x1866B79F79A10C90L;

  @Override public String description() {
    return "Consolidate declaration of " + name + " with its subsequent initialization";
  }

  @Override public Examples examples() {
    return //
    convert("int a; a = 3; fragment(b); fragment(a,b);a = fragment(a,b); b= fragment(a,b);}")//
        .to("int a = 3; fragment(b); fragment(a,b);a = fragment(a,b); b= fragment(a,b);");
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.eq(name, to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newInitializer = copy.of(from(a));
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement, g);
    return $;
  }
}
