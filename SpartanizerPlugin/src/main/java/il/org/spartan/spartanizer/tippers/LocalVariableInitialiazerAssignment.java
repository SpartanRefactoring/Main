package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class LocalVariableInitialiazerAssignment extends LocalVariableInitializedStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 1758294600103038096L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + trivia.gist(¢.getName()) + " with its subsequent initialization";
  }

  @Override public Example[] examples() {
    return new Example[] { //
        convert("int a; a = 3; fragment(b); fragment(a,b);a = fragment(a,b); b= fragment(a,b);}")//
            .to("int a = 3; fragment(b); fragment(a,b);a = fragment(a,b); b= fragment(a,b);") };
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(name, to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newInitializer = copy.of(from(a));
    if (LocalVariable.doesUseForbiddenSiblings(fragment, newInitializer))
      return null;
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement, g);
    return $;
  }
}
