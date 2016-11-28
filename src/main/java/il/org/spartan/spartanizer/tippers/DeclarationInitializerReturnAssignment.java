package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** Converts <code>int a=3;return a;</code> into <code>return 3;</code>
 * @author Yossi Gil
 * @since 2015-08-07
 * @DisableSpartan */
public final class DeclarationInitializerReturnAssignment extends $VariableDeclarationFragementAndStatement implements TipperCategory.Collapse {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate temporary '" + ¢.getName() + "', inlining its value into the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
        if (initializer == null || haz.annotation(f))
          return null;
        final ReturnStatement s = az.returnStatement(nextStatement);
        if (s == null)
          return null;
        final Assignment a = az.assignment(step.expression(s));
        if (a == null || !wizard.same(n, to(a)) || a.getOperator() != ASSIGN)
          return null;
        final Expression newReturnValue = duplicate.of(from(a));
        final InlinerWithValue i = new Inliner(n, r, g).byValue(initializer);
        if (!i.canInlineinto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving(f) - metrics.size(newReturnValue) > 0)
          return null;
        r.replace(a, newReturnValue, g);
        i.inlineInto(newReturnValue);
        eliminate(f, r, g);
        return r;
      }
}
