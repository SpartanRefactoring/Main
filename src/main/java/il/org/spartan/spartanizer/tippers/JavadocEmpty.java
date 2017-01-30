package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil
 * @since 2016-12-14 */
public final class JavadocEmpty extends RemovingTipper<Javadoc>//
    implements TipperCategory.SyntacticBaggage {
  @Override public String description(@SuppressWarnings("unused") final Javadoc __) {
    return "Remove empty Javadoc comment";
  }

  @Override public boolean prerequisite(final Javadoc ¢) {
    return tags(¢).stream().allMatch(JavadocEmpty::empty);
  }

  private static boolean empty(final TagElement e) {
    return fragments(e).stream().allMatch(λ -> λ == null || λ instanceof SimpleName || !(λ instanceof TextElement) || empty((TextElement) λ));
  }

  private static boolean empty(final TextElement ¢) {
    return ¢.getText().replaceAll("[\\s*]", "").isEmpty();
  }
}
