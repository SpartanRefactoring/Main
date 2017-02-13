package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil
 * @since 2016-12-14 */
public final class JavadocEmptyRemove extends RemovingTipper<Javadoc>//
    implements TipperCategory.SyntacticBaggage {
  @Override public String description(final Javadoc ¢) {
    return String.format("Remove empty Javadoc comment of %d characters", box.it(metrics.length(¢)));
  }

  @Override public boolean prerequisite(final Javadoc ¢) {
    return iz.empty(¢);
  }
}
