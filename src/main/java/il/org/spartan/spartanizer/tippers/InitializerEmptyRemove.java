package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Restructuring
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-01-21 */
public final class InitializerEmptyRemove extends RemovingTipper<Initializer>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 8587376936334392420L;

  @Override protected boolean prerequisite(final Initializer ¢) {
    final Block $ = ¢.getBody();
    return ¢.getJavadoc() == null && ($ == null || statements($).isEmpty());
  }

  @Override public String description() {
    return "Remove empty initializer";
  }

  @Override public String description(final Initializer ¢) {
    return "Remove empty " + (iz.static¢(¢) ? "" : "non-") + "static initializer";
  }
}
