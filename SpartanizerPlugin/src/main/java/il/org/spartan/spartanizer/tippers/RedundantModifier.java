package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class RedundantModifier extends CarefulTipper<Modifier> implements TipperCategory.SyntacticBaggage {
  @Override public String description(final Modifier ¢) {
    return "Eliminate redundant [" + ¢ + "] modifier";
  }
  @Override public String description() {
    return "Eliminate redundant modifier";
  }
  @Override public Tip tip(final Modifier ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite x = r.getListRewrite(parent(¢), az.bodyDeclaration(parent(¢)).getModifiersProperty());
        x.remove(¢, g);
      }
    };
  }
  @Override public boolean prerequisite(final Modifier ¢) {
    return test(¢, redundancies(az.bodyDeclaration(parent(¢))));
  }
}
