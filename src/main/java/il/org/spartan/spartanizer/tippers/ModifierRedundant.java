package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code abstract</b> <b>interface</b>a{}</code> to
 * {@code interface</b> a{}</code>, etc.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-29 */
public final class ModifierRedundant extends CarefulTipper<Modifier>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x131323DBCDA3200DL;

  @Override public String description(final Modifier ¢) {
    return "Remove redundant [" + ¢ + "] modifier";
  }

  @Override public String description() {
    return "Remove redundant modifier";
  }

  @Override public Tip tip(final Modifier ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.getListRewrite(parent(¢), az.bodyDeclaration(parent(¢)).getModifiersProperty()).remove(¢, g);
      }
    };
  }

  @Override public boolean prerequisite(final Modifier ¢) {
    return ModifiersRedundancy.test(¢, ModifiersRedundancy.redundancies(az.bodyDeclaration(parent(¢))));
  }
}
