package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code> if (a){g();}</code> into <code>if(a)g();</code>
 * @author Yossi Gil
 * @since 2016-12-14 */
public final class JavadocEmpty extends CarefulTipper<Javadoc> implements TipperCategory.SyntacticBaggage {
  @Override public String description(@SuppressWarnings("unused") final Javadoc __) {
    return "Remove empty Javadoc comment";
  }

  /** [[SuppressWarningsSpartan]] */
  @Override public boolean prerequisite(final Javadoc ¢) {
    final List<TagElement> tags = tags(¢);
    for (final TagElement t : tags)
      if (!empty(t))
        return false;
    return true;
  }

  private static boolean empty(final TagElement ¢) {
    for (final Object a : ¢.fragments())
      if (a != null && (!(a instanceof TextElement) || !empty((TextElement) a)))
        return false;
    return true;
  }

  private static boolean empty(final TextElement ¢) {
    return ¢.getText().replaceAll("[\\s*]", "").isEmpty();
  }

  @Override public Tip tip(final Javadoc i) {
    return new Tip(description(i), i, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }
}
