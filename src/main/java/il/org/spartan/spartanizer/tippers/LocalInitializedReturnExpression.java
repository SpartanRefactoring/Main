package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.patterns.*;

/** convert {@code int a = 3;return a;} into {@code return a;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class LocalInitializedReturnExpression extends LocalVariableInitializedStatement  //
    implements TipperCategory.Inlining {
  public LocalInitializedReturnExpression() {
    andAlso("Not used in subsequent initializers",
        () -> !usedInSubsequentInitializers() 
        ) ;// 
  }

  private static final long serialVersionUID = 0xECFC7713ABB0D4AL;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate local " + ¢.getName() + " and inline its value into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null)
      return null;
    final Expression newReturnValue = s.getExpression();
    if (newReturnValue == null)
      return null;
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (wizard.eq(name, newReturnValue) || !i.canSafelyInlineinto(newReturnValue)
        || i.replacedSize(newReturnValue) - eliminationSaving() - metrics.size(newReturnValue) > 0)
      return null;
    $.replace(s.getExpression(), newReturnValue, g);
    i.inlineInto(newReturnValue);
    il.org.spartan.spartanizer.ast.factory.remove.deadFragment(current, $, g);
    return $;
  }

}
