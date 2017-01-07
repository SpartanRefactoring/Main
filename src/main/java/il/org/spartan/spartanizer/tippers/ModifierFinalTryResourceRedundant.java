package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierFinalTryResourceRedundant extends CarefulTipper<Modifier> implements TipperCategory.SyntacticBaggage {
  @Override public String description() {
    return "Remove redundant final modifier of try resource";
  }

  @Override public String description(final Modifier ¢) {
    return "Remove redundant final modifier of '" + az.variableDeclarationExpression(parent(¢)) + "' (a try resource)";
  }

  @Override public Tip tip(final Modifier $) {
    if (!$.isFinal())
      return null;
    final VariableDeclarationExpression x = az.variableDeclarationExpression(parent($));
    return x == null || az.tryStatement(parent(x)) == null ? null : new Tip(description($), $, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove($, g);
      }
    };
  }
}
