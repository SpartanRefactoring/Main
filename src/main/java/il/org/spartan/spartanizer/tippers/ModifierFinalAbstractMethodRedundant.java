package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Yossi Gil
 * @since 2015-07-29 */
public final class ModifierFinalAbstractMethodRedundant extends CarefulTipper<Modifier> implements TipperCategory.SyntacticBaggage {
  @Override public String description() {
    return "Remove redundant final modifier of paramaeter to abstract method";
  }

  @Override public String description(final Modifier ¢) {
    return "Remove redundant final '" + az.singleVariableDeclaration(parent(¢)) + "' (parameter to abstract method)";
  }

  @Override public Tip tip(final Modifier m) {
    if (!m.isFinal())
      return null;
    final SingleVariableDeclaration v = az.singleVariableDeclaration(parent(m));
    if (v == null)
      return null;
    final MethodDeclaration $ = az.methodDeclaration(parent(v));
    return $ == null || body($) != null ? null : remove(m);
  }

  private Tip remove(final Modifier m) {
    return new Tip(description(m), m, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(m, g);
      }
    };
  }
}
