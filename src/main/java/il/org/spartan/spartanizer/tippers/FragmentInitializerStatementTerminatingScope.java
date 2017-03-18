package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.namespace.*;

/** Convert {@code int a=3;b=a;} into {@code b = a;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerStatementTerminatingScope extends $FragementAndStatement //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -221763355000543721L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline local '" + ¢.getName() + "' into subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
        if (haz.annotation(f) || wizard.frobiddenOpOnPrimitive(f, nextStatement) || wizard.isArrayInitWithUnmatchingTypes(f) || initializer == null)
          return null;
        final Inliner r = Inliner.of(n).by(initializer).in(scope.of(n));
        if (!r.ok())
          return null;
        $.remove(f.getParent(), g);
        return r.fire($, g);
      }
}
